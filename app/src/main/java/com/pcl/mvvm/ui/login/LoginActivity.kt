package com.pcl.mvvm.ui.login

import android.os.Bundle
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.ActivityLoginBinding

/**
 * @author PC
 * @date 2020/05/25 11:01
 */
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.loginViewModel = viewModel
    }

    override fun initData() {
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            0 -> {
//                tv_code.isEnabled = true
//                tv_code.setTextColor(ContextCompat.getColor(this,R.color.tv_gray))
            }
        }
    }
}