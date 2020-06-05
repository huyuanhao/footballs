/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jime.stu.ui.photo


import EXTENSION_WHITELIST
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.OrientationEventListener
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.Metadata
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.example.zhouwei.library.CustomPopWindow
import com.jime.stu.R
import com.jime.stu.bean.ImageDetail
import com.jime.stu.network.api.HomeService
import com.jime.stu.ui.MainActivity
import com.jime.stu.ui.MainActivity.Companion.KEY_EVENT_ACTION
import com.jime.stu.ui.MainActivity.Companion.KEY_EVENT_EXTRA
import com.jime.stu.utils.*
import com.jime.stu.utils.GlideEngine.createGlideEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

/**
 * Main fragment for this app. Implements all camera operations including:
 * - Viewfinder
 * - Photo taking
 * - Image analysis
 */
class CameraFragment : BaseFragment<CameraViewModel, ViewDataBinding>() {

    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView
    private lateinit var outputDirectory: File
    private lateinit var broadcastManager: LocalBroadcastManager

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var fragment: Fragment;

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    /** 使用此执行器执行阻塞摄像机操作 */
    private lateinit var cameraExecutor: ExecutorService

    /** 用于触发快门的音量降低按钮接收器 */
    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                // When the volume down button is pressed, simulate a shutter button click
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    val shutter = container
                        .findViewById<ImageButton>(R.id.camera_capture_button)
                    shutter.simulateClick()
                }
            }
        }
    }

    /**
    对于不触发配置更改的方向更改，我们需要一个显示侦听器，例如，如果我们选择覆盖清单中的配置更改或180度方向更改。
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@CameraFragment.displayId) {
                Log.d(TAG, "Rotation changed: ${view.display.rotation}")
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
                ToastUtils.showShort("Rotation changed: ${view.display.rotation}")
            }
        } ?: Unit
    }

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
//        if (!PermissionsFragment.hasPermissions(requireContext())) {
//            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
//                    CameraFragmentDirections.actionCameraToPermissions()
//            )
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewFinder.removeAllViews()
        // 关闭我们的后台执行器
        cameraExecutor.shutdown()

        // 注销广播接收器和侦听器
        broadcastManager.unregisterReceiver(volumeDownReceiver)
        displayManager.unregisterDisplayListener(displayListener)
    }

    private fun setGalleryThumbnail(uri: Uri) {
        // 包含库缩略图的视图的引用
        val thumbnail = container.findViewById<ImageView>(R.id.photo_view_button)

        // 在视图的线程中运行操作
        thumbnail.post {

            // 删除缩略图填充
//            thumbnail.setPadding(resources.getDimension(R.dimen.stroke_small).toInt())

            // Load thumbnail into circular button using Glide
            ImageLoaderManager.loadUriWithCenterCrop(activity, uri, thumbnail)
//            Glide.with(thumbnail)
//                .load(uri)
////                .apply(RequestOptions.circleCropTransform())
//                .into(thumbnail)
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_camera
    }


    private val mService by lazy { RetrofitClient.getInstance().create(HomeService::class.java) }
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment = this
        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.view_finder)

        // 初始化我们的后台执行器
        cameraExecutor = Executors.newSingleThreadExecutor()

        broadcastManager = LocalBroadcastManager.getInstance(view.context)

        // 设置将从主活动接收事件的意图筛选器
        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        broadcastManager.registerReceiver(volumeDownReceiver, filter)

        // 每次设备的方向改变时，更新用例的旋转
        displayManager.registerDisplayListener(displayListener, null)

        // 确定输出目录
        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        // 等待视图正确布局
        viewFinder.post {

            // 跟踪附加此视图的显示
            displayId = viewFinder.display.displayId

            // 生成UI控件
            updateCameraUi()

            // 设置相机及其用例
            setUpCamera()
        }

        val ac = activity as MainActivity;
        ac.setOnToInputClick(object : MainActivity.OnToInputClick {
            override fun toInput() {
                val v = layoutInflater.inflate(R.layout.dialog_input, null)
                val tvSearch = v.findViewById<TextView>(R.id.tv_search)
                val edit = v.findViewById<EditText>(R.id.edit)
                edit.setOnLongClickListener(object : View.OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {
                        edit.setText(ClipeBoardUtil.getClipeBoardContent(activity))
                        return true
                    }

                })
                tvSearch.setOnClickListener {
                    if (TextUtils.isEmpty(edit.text.toString())) {
                        ToastUtils.showShort("网址不能为空")
                        return@setOnClickListener
                    }

                    viewModel.uploadUrl(edit.text.toString())
                }
                CustomPopWindow.PopupWindowBuilder(activity)
                    .setView(v)//显示的布局，还可以通过设置一个View
                    //     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                    .setFocusable(true)//是否获取焦点，默认为ture
                    .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                    .create()//创建PopupWindow
                    .showAtLocation(container, Gravity.CENTER, 0, 0);//显示PopupWindow
            }

        })
    }

    /**
     * Inflate camera controls and update the UI manually upon config changes to avoid removing
     * and re-adding the view finder from the view hierarchy; this provides a seamless rotation
     * transition on devices that support it.
     * NOTE: The flag is supported starting in Android 8 but there still is a small flash on the
     * screen for devices that run Android 9 or below.
     * 在配置更改时手动对相机控件充气并更新用户界面，以避免从视图层次结构中删除和重新添加取景器；这在支持it.
     * 注意：从Android 8开始支持该标志，但对于运行Android 9或更低版本的设备，屏幕上仍有一个小的闪光灯
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Redraw the camera UI controls
        //重新绘制相机用户界面控件
        updateCameraUi()

        // 启用或禁用摄像机之间的切换
        updateCameraSwitchButton()
    }

    /** 初始化CameraX，并准备绑定camera用例  */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // 根据可用的相机选择镜头对齐
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            // 启用或禁用摄像机之间的切换
            updateCameraSwitchButton()

            // 构建并绑定相机用例
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /** 声明和绑定预览、捕获和分析用例*/
    private fun bindCameraUseCases() {

        // 获取用于设置摄像机全屏分辨率的屏幕度量
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = viewFinder.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
            // 我们要求纵横比但没有分辨率
            .setTargetAspectRatio(screenAspectRatio)
            // 设置初始目标旋转
            .setTargetRotation(rotation)
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//        我们请求纵横比，但没有与预览配置匹配的分辨率，但是允许CameraX优化任何最适合我们用例的特定分辨率
            .setTargetAspectRatio(screenAspectRatio)
//        设置初始目标旋转，如果在这个用例的生命周期中旋转发生变化，我们将不得不再次调用它
            .setTargetRotation(rotation)
            .build()

        // 图像分析
        imageAnalyzer = ImageAnalysis.Builder()
            // 我们要求纵横比但没有分辨率
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(rotation)
            .build()
            // 然后可以将分析器分配给实例
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                    //                        从我们的分析器返回的值被传递到附加的侦听器我们在这里记录图像分析结果-你应该做一些有用的事情来代替！

                    Log.d(TAG, "Average luminosity: $luma")
                })
            }

        // 在重新绑定用例之前必须解除绑定
        cameraProvider.unbindAll()

        try {
//            这里可以传递不同数量的用例-camera提供对CameraControl和CameraInfo的访问
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )

            // Attach the viewfinder's surface provider to preview use case
//            将取景器的表面提供程序附加到预览用例
            preview?.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }

        var mAlbumOrientationEventListener = AlbumOrientationEventListener(activity);

    }

    /**
     *  [androidx.camera.core.ImageAnalysisConfig] 需要枚举值
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *  通过计算预览比率的绝对值与提供的值之一，检测@params中提供的维度的最合适比率。
     *  @param width - preview width
     *  @param height - preview height
     *  @return 合适的长宽比
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /** 用于重新绘制camera UI控件的方法，每次配置更改时调用该方法. */
    private fun updateCameraUi() {

        // 删除上一个用户界面（如果有）
        container.findViewById<ConstraintLayout>(R.id.camera_ui_container)?.let {
            container.removeView(it)
        }

        // 为包含用于控制相机的所有用户界面的新视图充气
        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

        //在后台，加载库缩略图的最新照片（如果有的话）
        lifecycleScope.launch(Dispatchers.IO) {
            outputDirectory.listFiles { file ->
                EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
            }?.max()?.let {
                setGalleryThumbnail(Uri.fromFile(it))
            }
        }

        // 用于捕获照片的按钮的侦听器
        controls.findViewById<ImageButton>(R.id.camera_capture_button).setOnClickListener {

            //获取可修改图像捕获用例的稳定参考
            imageCapture?.let { imageCapture ->

                // 创建用于保存图像的输出文件
                val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)

                // 设置图像捕获元数据
                val metadata = Metadata().apply {

                    //使用前摄像头时镜像
                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }

                // 创建包含文件+元数据的输出选项对象
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                    .setMetadata(metadata)
                    .build()

                // 设置拍照后触发的图像捕获侦听器
                imageCapture.takePicture(
                    outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                            Log.d(TAG, "Photo capture succeeded: $savedUri")
                            savedUri.toFile()
                            toCrop(savedUri)

                            // We can only change the foreground Drawable using API level 23+ API
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            用最新拍摄的图片更新库缩略图
                                setGalleryThumbnail(savedUri)
                            }

                            // Implicit broadcasts will be ignored for devices running API level >= 24
                            // so if you only target API level 24+ you can remove this statement
//                        对于运行API级别>=24的设备，将忽略隐式广播
//                                因此，如果只针对API级别24+，则可以删除此语句
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                requireActivity().sendBroadcast(
                                    Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri)
                                )
                            }

//                            如果选定的文件夹是外部媒体目录，则这是不必要的，但如果不使用[MediaScannerConnection]扫描图像，则其他应用程序将无法访问我们的图像
                            val mimeType = MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension(savedUri.toFile().extension)
                            MediaScannerConnection.scanFile(
                                context,
                                arrayOf(savedUri.toFile().absolutePath),
                                arrayOf(mimeType)
                            ) { _, uri ->
                                //                                ToastUtils.showShort("Image capture")
                                Log.d(TAG, "Image capture scanned into media store: $uri")
                            }
//                            ToastUtils.showShort(imageCapture.targetRotation)
                        }
                    })

                // We can only change the foreground Drawable using API level 23+ API
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // 显示flash动画以指示照片已被捕获
                    container.postDelayed({
                        container.foreground = ColorDrawable(Color.WHITE)
                        container.postDelayed(
                            { container.foreground = null }, ANIMATION_FAST_MILLIS
                        )
                    }, ANIMATION_SLOW_MILLIS)
                }
            }
        }

        // Setup for button used to switch cameras
        controls.findViewById<ImageButton>(R.id.camera_switch_button).let {
            // Disable the button until the camera is set up
            it.isEnabled = false

            // Listener for button used to switch cameras. Only called if the button is enabled
            it.setOnClickListener {
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofAll())
                    .imageEngine(createGlideEngine())
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .isSingleDirectReturn(true)//单选直接返回
                    .isCamera(false)
                    .isEnableCrop(false)// 是否裁剪
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: MutableList<LocalMedia>?) {
                            var media = result?.get(0)
//                            var intent = Intent(activity, PhotoActivity::class.java)
//                            intent.putExtra("file", media?.getPath().toString())
//                            startActivity(intent)
                            toCrop(Uri.fromFile(File(media?.path)))
                            Log.i(TAG, "是否压缩:" + media?.isCompressed());
                            Log.i(TAG, "压缩:" + media?.getCompressPath());
                            Log.i(TAG, "原图:" + media?.getPath());
                            Log.i(TAG, "是否裁剪:" + media?.isCut());
                            Log.i(TAG, "裁剪:" + media?.getCutPath());
                            Log.i(TAG, "是否开启原图:" + media?.isOriginal());
                            Log.i(TAG, "原图路径:" + media?.getOriginalPath());
                            Log.i(TAG, "Android Q 特有Path:" + media?.getAndroidQToPath());
                            Log.i(TAG, "宽高: " + media?.getWidth() + "x" + media?.getHeight());
                            Log.i(TAG, "Size: " + media?.getSize());
                        }

                        override fun onCancel() {
                        }
                    });
//                lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
//                    CameraSelector.LENS_FACING_BACK
//                } else {
//                    CameraSelector.LENS_FACING_FRONT
//                }
                // 重新绑定用例以更新选定的相机
//                bindCameraUseCases()
                // 设置相机及其用例
                setUpCamera()
            }
        }

        // 用于查看最新照片的按钮的侦听器
        controls.findViewById<ImageView>(R.id.photo_view_button).setOnClickListener {
            // Only navigate when the gallery has photos
            if (true == outputDirectory.listFiles()?.isNotEmpty()) {
//                Navigation.findNavController(
//                        requireActivity(), R.id.fragment_container
//                ).navigate(CameraFragmentDirections
//                        .actionCameraToGallery(outputDirectory.absolutePath))
                var intent = Intent(activity, GalleryActivity::class.java)
                intent.putExtra("path", outputDirectory.absolutePath)
                startActivity(intent)
            }
        }
    }

    fun toCrop( savedUri: Uri){
       var outCropUrl = context?.let { MainActivity.getOutputDirectory(it) }
        var mDestinationUri =
            Uri.fromFile(File(outputDirectory, "图" + savedUri.toFile().name));
        var uCrop = UCrop.of(savedUri, mDestinationUri);
        var options = UCrop.Options();
        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5f);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置是否展示矩形裁剪框
        options.setShowCropFrame(true);
        //设置裁剪框横竖线的宽度
        options.setCropGridStrokeWidth(1);
        options.setHideBottomControls(true)
        //设置裁剪框横竖线的颜色
        options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        options.setCropGridColumnCount(2);
        //设置横线的数量
        options.setCropGridRowCount(1);
        //结束设置
        uCrop.withOptions(options)
        activity?.let { uCrop.start(it, this@CameraFragment) };
    }

    /** 启用或禁用根据可用摄像机切换摄像机的按钮 */
    private fun updateCameraSwitchButton() {
        val switchCamerasButton = container.findViewById<ImageButton>(R.id.camera_switch_button)
        try {
            switchCamerasButton.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            switchCamerasButton.isEnabled = false
        }
    }

    /** 如果设备有可用的后摄像头，则返回true。否则为假 */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** 如果设备有可用的前置摄像头，则返回true。否则为假*/
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    /**
     * 我们的自定义图像分析类。
     *
    <p>我们需要做的就是用我们想要的操作覆盖函数“analyze”。在这里，我们通过观察YUV帧的Y平面来计算图像的平均亮度。
     */
    private class LuminosityAnalyzer(listener: LumaListener? = null) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private val listeners = ArrayList<LumaListener>().apply { listener?.let { add(it) } }
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        /**
         * 用于添加将在计算每个luma时调用的侦听器
         */
        fun onFrameAnalyzed(listener: LumaListener) = listeners.add(listener)

        /**
         * 用于从图像平面缓冲区提取字节数组的助手扩展函数
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // 将缓冲区倒回零
            val data = ByteArray(remaining())
            get(data)   // 将缓冲区复制到字节数组中
            return data // Return the byte array
        }

        /**
         * Analyzes an image to produce a result.
         *
         * <p>The caller is responsible for ensuring this analysis method can be executed quickly
         * enough to prevent stalls in the image acquisition pipeline. Otherwise, newly available
         * images will not be acquired and analyzed.
         *
         * <p>The image passed to this method becomes invalid after this method returns. The caller
         * should not store external references to this image, as these references will become
         * invalid.
         *
         * @param image image being analyzed VERY IMPORTANT: Analyzer method implementation must
         * call image.close() on received images when finished using them. Otherwise, new images
         * may not be received or the camera may stall, depending on back pressure setting.
         * 分析图像以产生结果。
        <p>调用方负责确保此分析方法能够足够快地执行，以防止图像采集管道中出现暂停。否则，将不会获取和分析新可用的图像。
        <p>传递给此方法的图像在此方法返回后无效。调用方不应存储对此映像的外部引用，因为这些引用将变得无效。
        @正在分析的参数图像非常重要：分析器方法实现必须调用图像.关闭（在使用完接收的图像后）。否则，根据背压设置，可能无法接收新图像或相机可能失速。
         *
         */
        override fun analyze(image: ImageProxy) {
            // 如果没有附加侦听器，则不需要执行分析
            if (listeners.isEmpty()) {
                image.close()
                return
            }

            // 跟踪分析的帧
            val currentTime = System.currentTimeMillis()
            frameTimestamps.push(currentTime)

            // 使用移动平均值计算FPS
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
            val timestampLast = frameTimestamps.peekLast() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

            // 分析可能需要任意长的时间
            // 因为我们运行在不同的线程中，所以它不会延迟其他用例

            lastAnalyzedTimestamp = frameTimestamps.first

            // 因为ImageAnalysis的格式是YUV，图像.平面[0]包含亮度平面
            val buffer = image.planes[0].buffer

            // 从回调对象中提取图像数据
            val data = buffer.toByteArray()

            // 将数据转换为0-255范围内的像素值数组
            val pixels = data.map { it.toInt() and 0xFF }

            // 计算图像的平均亮度
            val luma = pixels.average()

            // 用新值调用所有侦听器
            listeners.forEach { it(luma) }

            image.close()
        }
    }

    companion object {
        fun newInstance() = CameraFragment()
        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** 用于创建时间戳文件的Helper函数 */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(
                baseFolder, SimpleDateFormat(format, Locale.US)
                    .format(System.currentTimeMillis()) + extension
            )
    }


    class AlbumOrientationEventListener(context: Context?) : OrientationEventListener(context) {
        var mOrientation = 0

        override fun onOrientationChanged(orientation: Int) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                return;
            }

            //保证仅仅返回四个方向
            var newOrientation = ((orientation + 45) / 90 * 90) % 360
            if (newOrientation != mOrientation) {
                mOrientation = newOrientation;

                //返回的mOrientation就是手机方向，为0°、90°、180°和270°中的一个
            }
        }
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            1 -> {
                var detail = msg.obj as ImageDetail
                var intent = Intent(activity, PhotoResultActivity::class.java)
                intent.putExtra("detail", detail)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            if (requestCode == UCrop.REQUEST_CROP) {
//                var resultUri = data?.let { UCrop.getOutput(it) };
//                var intent = Intent(activity, PhotoResultActivity::class.java)
//                intent.putExtra("file", resultUri.toFile())
//                startActivity(intent)
//            }
//        }
    }
}
