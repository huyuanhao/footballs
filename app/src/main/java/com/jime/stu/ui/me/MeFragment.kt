package com.jime.stu.ui.me

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jime.stu.R
import com.jime.stu.bean.MeInfo
import com.jime.stu.databinding.MeFragmentBinding
import com.jime.stu.network.entity.UsedWeb
import com.jime.stu.ui.detail.DetailActivity
import com.jime.stu.ui.login.LoginActivity
import com.jime.stu.utils.Preference
import kotlinx.android.synthetic.main.me_fragment.*

class MeFragment : BaseFragment<MeViewModel, MeFragmentBinding>() {
    var userId by Preference(Preference.USER_ID, "")
    var userName by Preference(Preference.USER_NAME, "")
    var headImg by Preference(Preference.HEADIMAGE, "")
    var token by Preference(Preference.TOKEN, "")
    var is_login by Preference(Preference.IS_LOGIN, false)
    private val mAdapter by lazy { MeWebAdapter() }

    companion object {
        fun newInstance() = MeFragment()
    }

    override fun layoutId() = R.layout.me_fragment

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.meViewModel = viewModel
        mBinding?.fragment = this
    }


    override fun lazyLoadData() {
        viewModel.getItemList()
    }

    fun onFeelBackClick() {
        ToastUtils.showShort("反馈")
    }
    fun onSharedClick() {
        ToastUtils.showShort("分享")
    }
    fun onAgreementClick() {
        ToastUtils.showShort("协议")
    }
    fun onMessageClick() {
        ToastUtils.showShort("消息")
    }
    fun onToLoginClick() {
        if(!is_login) {
            startActivityForResult(Intent(activity, LoginActivity::class.java), 100)
        }
    }
    fun onExitClick() {//退出
        activity?.let {
            MaterialDialog(it).show {
                title(R.string.login_exit)
                message(R.string.are_you_sure_to_exit)
                positiveButton(R.string.ok){
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
        when(msg.code){
            1->{
                var info = msg.obj as MeInfo
                startActivity(Intent(activity,DetailActivity::class.java).putExtra("url",info.wordUrl))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == 101){
            lazyLoadData()
        }
    }
}
