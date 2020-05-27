package com.jime.stu.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jime.stu.R
import com.jime.stu.ui.guide.GuideActivity
import com.jime.stu.ui.login.LoginActivity
import com.jime.stu.utils.Preference

/**
 * @author PC
 * @date 2020/05/25 10:41
 */
class WelcomeActivity :Activity(){
    var isFirst by Preference(Preference.IS_FIRST, true)
    val isLogin by Preference(Preference.IS_LOGIN, false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        if(isFirst){
            startActivity(Intent(this, GuideActivity::class.java))
            isFirst = false
        }else{
//            if(isLogin){
                startActivity(Intent(this, MainActivity::class.java))
//            }else {
//                startActivity(Intent(this, LoginActivity::class.java))
//            }
        }
        finish()
    }
}