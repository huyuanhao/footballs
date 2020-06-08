package com.jime.stu.ui.guide

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.jime.stu.R
import com.jime.stu.ui.MainActivity
import com.jime.stu.utils.Preference
import com.jime.stu.widget.MyWebView
import com.umeng.message.PushAgent

/**
 * @author PC
 * @date 2020/05/25 10:41
 */
class WelcomeActivity :BaseActivity<WelcomeModel,ViewDataBinding>(){
    var isFirst by Preference(Preference.IS_FIRST, true)
    val isLogin by Preference(Preference.IS_LOGIN, false)
    override fun layoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.getAppInfo()
        MyWebView
    }

    override fun initData() {
//        if(isFirst){
//            startActivity(Intent(this, GuideActivity::class.java))
//            isFirst = false
//        }else{
////            if(isLogin){
//            startActivity(Intent(this, MainActivity::class.java))
////            }else {
////                startActivity(Intent(this, LoginActivity::class.java))
////            }
//        }
//        finish()
    }

    override fun handleEvent(msg: Message) {
        when(msg.code){
            1->{
                if(isFirst){
                    startActivity(Intent(this, GuideActivity::class.java))
                    isFirst = false
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }
            else ->{
                if(isFirst){
                    startActivity(Intent(this, GuideActivity::class.java))
                    isFirst = false
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }

        }
    }
}