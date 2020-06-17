package com.jime.stu.ui.me

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.bean.MeInfo
import com.jime.stu.databinding.MeFragmentBinding
import com.jime.stu.share.ShareActivity
import com.jime.stu.ui.detail.DetailActivity
import com.jime.stu.ui.login.LoginActivity
import com.jime.stu.ui.photo.CameraFragment
import com.jime.stu.utils.GlideEngine
import com.jime.stu.utils.Preference
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.me_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


class MeFragment : BaseFragment<MeViewModel, MeFragmentBinding>() {
    var userId by Preference(Preference.USER_ID, "")
    var userName by Preference(Preference.USER_NAME, "")
    var headImg by Preference(Preference.HEADIMAGE, "")
    var token by Preference(Preference.TOKEN, "")
    var is_login by Preference(Preference.IS_LOGIN, false)
    var url by Preference(
        Preference.UNLOCKURL, "https://photoapi.jimetec.com/shitu/dist/pay.html"
    )
    private val mAdapter by lazy { MeWebAdapter() }

    companion object {
        fun newInstance() = MeFragment()
    }

    override fun layoutId() = R.layout.me_fragment

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.meViewModel = viewModel
        mBinding?.fragment = this
        EventBus.getDefault().register(this)
    }

    override fun lazyLoadData() {
        getItemList()
    }

    fun getItemList(){
        viewModel.getItemList()
    }

    //头像点击
    fun onHeadImageClick() {
        if (is_login) {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isSingleDirectReturn(true)//单选直接返回
                .isCamera(true)
                .isCompress(true)
                .isEnableCrop(true)// 是否裁剪
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        var media = result?.get(0)
//                            var intent = Intent(activity, PhotoActivity::class.java)
//                            intent.putExtra("file", media?.getPath().toString())
//                            startActivity(intent)
                        Log.i("headCamera", "是否压缩:" + media?.isCompressed());
                        Log.i("headCamera", "压缩:" + media?.getCompressPath());
                        Log.i("headCamera", "原图:" + media?.getPath());
                        Log.i("headCamera", "是否裁剪:" + media?.isCut());
                        Log.i("headCamera", "裁剪:" + media?.getCutPath());
                        Log.i("headCamera", "是否开启原图:" + media?.isOriginal());
                        Log.i("headCamera", "原图路径:" + media?.getOriginalPath());
                        Log.i("headCamera", "Android Q 特有Path:" + media?.getAndroidQToPath());
                        Log.i("headCamera", "宽高: " + media?.getWidth() + "x" + media?.getHeight());
                        Log.i("headCamera", "Size: " + media?.getSize());
                        if (media != null) {
                            if (TextUtils.isEmpty(media?.compressPath)) {
                                viewModel.uploadFile(file = File(media?.getCutPath()))
                            } else {
                                viewModel.uploadFile(file = File(media?.getCompressPath()))
                            }
                        }
                    }

                    override fun onCancel() {
                    }
                });
        } else {
            startActivityForResult(Intent(activity, LoginActivity::class.java), 100)
        }
    }

    //开通会员
    fun openClick() {
        if (is_login) {
            viewModel.save("", 1, "会员按钮", "会员")
            startActivity(
                Intent(activity, WebActivity::class.java).putExtra(
                    "url",
                    url
                ).putExtra("title", "支付")
            )
        } else {
            startActivityForResult(Intent(activity, LoginActivity::class.java), 100)
        }
    }

    fun onFeelBackClick() {
        if (is_login) {
            startActivity(Intent(activity, FeelBackActivity::class.java))
        } else {
            startActivityForResult(Intent(activity, LoginActivity::class.java), 100)
        }
    }

    fun onSharedClick() {
        //                MyWebViewActivity.startToAfterVip(mContext);
//
        viewModel.save("", 1, "分享按钮", "分享")
        val share = Intent(activity, ShareActivity::class.java)
        startActivity(share)
    }

    fun onAgreementClick() {
        val url by Preference(Preference.PRIVATEAGREE, "")
        viewModel.save(url, 1, " 隐私协议按钮", "隐私协议")
        startActivity(
            Intent(activity, WebActivity::class.java)
                .putExtra("url", url).putExtra("title", "隐私协议")
        )
    }

    //消息
    fun onMessageClick() {
        viewModel.save("", 1, "消息按钮", "我的消息")
        startActivity(Intent(activity, MessageActivity::class.java))
    }

    fun onToLoginClick() {
        if (!is_login) {
            startActivityForResult(Intent(activity, LoginActivity::class.java), 100)
        }
    }

    fun onExitClick() {//退出
        activity?.let {
            MaterialDialog(it).show {
                title(R.string.login_exit)
                message(R.string.are_you_sure_to_exit)
                positiveButton(R.string.ok) {
                    userId = "0"
                    userName = ""
                    headImg = ""
                    token = ""
                    is_login = false
                    getItemList()
                }
                negativeButton(R.string.cancel)
            }
        }
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            1 -> {
                var info = msg.obj as MeInfo
                startActivity(
                    Intent(activity, DetailActivity::class.java).putExtra(
                        "url",
                        info.wordUrl
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getItemList()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 100 && resultCode == 101) {
//            lazyLoadData()
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    //登录后通知刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: String) {
//        if(message.equals("fresh")) {
//            lazyLoadData()
//        }else
        if (message.equals("message")) {
            ll_message.visibility = View.VISIBLE
        } else if (message.equals("nomessage")) {
            ll_message.visibility = View.GONE
        }
    }
}
