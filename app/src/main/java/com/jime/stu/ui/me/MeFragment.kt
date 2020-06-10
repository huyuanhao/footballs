package com.jime.stu.ui.me

import android.content.Intent
import android.os.Bundle
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
import com.jime.stu.utils.Preference
import kotlinx.android.synthetic.main.me_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MeFragment : BaseFragment<MeViewModel, MeFragmentBinding>() {
    var userId by Preference(Preference.USER_ID, "")
    var userName by Preference(Preference.USER_NAME, "")
    var headImg by Preference(Preference.HEADIMAGE, "")
    var token by Preference(Preference.TOKEN, "")
    var is_login by Preference(Preference.IS_LOGIN, false)
    var url by Preference(Preference.UNLOCKURL, "https://photoapi.jimetec.com/shitu/dist/pay.html"
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
        viewModel.getItemList()
    }

    //开通会员
    fun openClick() {
        if(is_login){
            startActivity(Intent(activity, WebActivity::class.java).putExtra("url", url).putExtra("title","支付"))
        }else{
            startActivityForResult(Intent(activity, LoginActivity::class.java), 100)
        }
    }

    fun onFeelBackClick() {
        startActivity(Intent(activity, FeelBackActivity::class.java))
    }

    fun onSharedClick() {
        //                MyWebViewActivity.startToAfterVip(mContext);
//
        val share = Intent(activity, ShareActivity::class.java)
        startActivity(share)
    }

    fun onAgreementClick() {
        val url by Preference(Preference.PRIVATEAGREE, "")
        startActivity(
            Intent(activity, WebActivity::class.java)
                .putExtra("url", url).putExtra("title", "隐私协议")
        )
    }

    //消息
    fun onMessageClick() {
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
                    lazyLoadData()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 101) {
            lazyLoadData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    //登录后通知刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMessage(message: String) {
        if(message.equals("fresh")) {
            lazyLoadData()
        }else if(message.equals("message")){
            ll_message.visibility = View.VISIBLE
        }else if(message.equals("nomessage")){
            ll_message.visibility = View.GONE
        }
    }
}
