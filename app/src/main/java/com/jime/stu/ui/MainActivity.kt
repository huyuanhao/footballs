package com.jime.stu.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayout
import com.jime.stu.MainViewModel
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.ui.me.MeFragment
import com.jime.stu.ui.photo.CameraFragment
import com.jime.stu.utils.Preference
import com.jime.stu.widget.ConfirmPopupView
import com.luck.picture.lib.permissions.PermissionChecker
import com.lxj.xpopup.XPopup
import com.umeng.message.PushAgent

import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class MainActivity : BaseActivity<MainViewModel,ViewDataBinding>(), EasyPermissions.PermissionCallbacks {
    /**
     * 展示服务协议和隐私政策
     */
    var userAgree by Preference(
        Preference.USERAGREE,
        "https://photoapi.jimetec.com/shitu/dist/agreement/userAgree.html"
    )
    var privateAgree by Preference(
        Preference.PRIVATEAGREE,
        "https://photoapi.jimetec.com/shitu/dist/agreement/privateAgree.html"
    )
    //是否已同意协议
    var isShowAgreement by Preference(Preference.ISSHOWAGREEMENT, false)
    private val titles = arrayOf("识图", "我的")
    val tabIcons = arrayOf(R.drawable.tab_home, R.drawable.tab_me)
    private val fragments = ArrayList<Fragment>()
    var old = 0;
    //是否已经授权
    private var isAllowPermissions = false;
    var perms = arrayOf(
        Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        //自定义函数检查权限是否拥有
        showAgreementDialog()
    }

    override fun initData() {
    }

    fun requestPermisson() {
        // 没有权限，进行权限请求
        EasyPermissions.requestPermissions(this, "为了正常使用，需要获取相机权限", 1, *perms);
    }

    @AfterPermissionGranted(1)
    private fun initView() {
        fragments.add(CameraFragment.newInstance())
//        fragments.add(ProjectFragment.newInstance())
        fragments.add(MeFragment.newInstance())
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragments[0])
            .commitNow()
//
////        val navController = findNavController(R.id.nav_host_fragment)
////        nav_view.setupWithNavController(navController)
////        //取消默认的图片颜色切换效果,改用自定义的selector
////        nav_view.itemIconTintList = null
////
//        nav_view.setOnNavigationItemSelectedListener(object :
//            BottomNavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
//                when (p0.itemId) {
//                    R.id.homeFragment ->
//                        if (old != 0) {
//                            switchPage(0, old);
//                            old = 0;
//                            return true
//                        }
//                    R.id.meFragment ->
//                        if (old != 1) {
//                            switchPage(1, old);
//                            old = 1;
//                            return true
//                        }
////                    R.id.meFragment->
////                        if (old != 2) {
////                            switchPage(2, old);
////                            old = 2;
////                            return true
////                        }
//                }
////                switchPage(index, old)
//                return false
//            }
//        })


//        nav_view.setOnNavigationItemSelectedListener(object :BottomNavigationView.OnNavigationItemSelectedListener{
//            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
//                when (p0.itemId) {
//                    R.id.homeFragment->{
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            MeFragmentDirections.actionMeToCamera())
//                        return true
//                    }
//                    R.id.meFragment->{
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            CameraFragmentDirections.actionCameraToMe())
//                        return true
//                    }
//                }
//                return false
//            }
//        })
        for (i in 0..1) { //为TabLayout添加10个tab并设置上文本
            tabLayout.addTab(tabLayout.newTab().setCustomView(getCustomView(i)))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                if (p0?.position == 0) {
                    toInput.toInput()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    0 -> {
                        viewModel.save("",1,"首页识图按钮 ","识图")
                        if (old != 0) {
                            switchPage(0, old);
                            old = 0;
                        }
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            MeFragmentDirections.actionMeToCamera())
                    }
                    1 -> {
                        viewModel.save("",1,"我的按钮","我的")
                        if (old != 1) {
                            switchPage(1, old);
                            old = 1;
                        }
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            CameraFragmentDirections.actionCameraToMe())
                    }
                }
            }

        })
    }

    fun getCustomView(i: Int): View {
        val rootView: View = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null)
        var img = rootView.findViewById<AppCompatImageView>(R.id.img_title)
        var tv = rootView.findViewById<TextView>(R.id.txt_title)
        img.setImageDrawable(ContextCompat.getDrawable(this, tabIcons[i]));
        tv.setText(titles[i])
        return rootView
    }

    private fun switchPage(index: Int, old: Int) {
        val now = fragments[index]
        supportFragmentManager.beginTransaction().apply {
            if (!now.isAdded) {
                add(R.id.container, now)
            }
            show(now)
            hide(fragments[old])
            commit()
        }
    }

    private fun showAgreementDialog() {
        if (isShowAgreement) {
            requestPermisson()
            return
        }
        val style = SpannableStringBuilder()
        //设置文字
        style.append(
            "请你务必审慎阅读、充分理解“服务及隐私政策”各条款，包括但不限于:为了向你提供位置等服务,我们需要收集你的设备信息、操作日志等个人信息。你可以在“设置”中查看、变更、删除个人信息并管理你的授权。\n" +
                    " 你可阅读《隐私协议》及《用户协议》了解详细信息。如你同意，请点击“同意\"开始接受我们的服务。"
        )
        //设置部分文字点击事件
        val clickableSpan: ClickableSpan =
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebActivity.startTo(
                        this@MainActivity,
                        privateAgree,
                        "隐私协议"
                    )
                }
            }
        //设置部分文字点击事件
        val clickableSpan2: ClickableSpan =
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebActivity.startTo(
                        this@MainActivity,
                        userAgree,
                        "用户协议"
                    )
                }
            }
        style.setSpan(clickableSpan, 103, 109, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        style.setSpan(clickableSpan2, 110, 116, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val popupView =
            ConfirmPopupView(this)
        popupView.setTitleContent("服务协议和隐私政策", style, null)
        popupView.setCancelText("暂不使用")
        popupView.setConfirmText("同意")
        popupView.setListener({
            ToastUtils.showShort("同意")
            isShowAgreement = true
            requestPermisson()
        }, { finish() })

        XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false)
            .asCustom(popupView).show()
    }

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
//        container.postDelayed({
//            container.systemUiVisibility = FLAGS_FULLSCREEN
//        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    companion object {
        val KEY_EVENT_ACTION = "key_event_action"
        val KEY_EVENT_EXTRA = "key_event_extra"
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L
        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }

    }

    lateinit var toInput: OnToInputClick;
    fun setOnToInputClick(toInput: OnToInputClick) {
        this.toInput = toInput
    }

    interface OnToInputClick {
        fun toInput();
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        /**
         * 若是在权限弹窗中，用户勾选了'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).setTitle("权限申请")
                .setRationale("万能识图需要获取相机权限，是否前往设置开启")
                .build().show();
        } else {
            finish()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //从设置页面返回，判断权限是否申请。
            if (EasyPermissions.hasPermissions(this, *perms)) {
                initView()
            } else {
//                AppSettingsDialog.Builder(this).setTitle("权限申请")
//                    .setRationale("万能识图需要获取相机权限，是否前往设置开启")
//                    .build().show();
                finish()
            }
        }
    }

}
