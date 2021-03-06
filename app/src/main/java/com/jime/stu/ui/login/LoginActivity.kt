package com.jime.stu.ui.login

import android.content.Intent
import android.os.Bundle
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.jime.stu.R
import com.jime.stu.databinding.ActivityLoginBinding
import com.jime.stu.ui.MainActivity
import kotlinx.android.synthetic.main.toorbar.*
import org.greenrobot.eventbus.EventBus

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
        tv_title.text = "登录"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            0 -> {
//                startActivity(Intent(this, MainActivity::class.java))
                setResult(101)
                EventBus.getDefault().post("fresh")
                finish()
//                tv_code.isEnabled = true
//                tv_code.setTextColor(ContextCompat.getColor(this,R.color.tv_gray))
            }
        }
    }
}