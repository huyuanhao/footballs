package com.pcl.mvvm.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pcl.mvvm.R
import com.pcl.mvvm.ui.guide.GuideActivity
import com.pcl.mvvm.ui.login.LoginActivity
import com.pcl.mvvm.utils.Preference

/**
 * @author PC
 * @date 2020/05/25 10:41
 */
class WelcomeActivity :Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val isFirst by Preference(Preference.IS_FIRST, true)
        val isLogin by Preference(Preference.IS_LOGIN, false)
        if(isFirst){
            startActivity(Intent(this, GuideActivity::class.java))
        }else{
            if(isLogin){
                startActivity(Intent(this, MainActivity::class.java))
            }else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        finish()
    }
}