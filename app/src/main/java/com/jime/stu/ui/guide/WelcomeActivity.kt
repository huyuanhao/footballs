package com.jime.stu.ui.guide

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.jime.stu.R
import com.jime.stu.ui.MainActivity
import com.jime.stu.utils.Preference
import com.jime.stu.widget.MyWebView
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.umeng.message.PushAgent

/**
 * @author PC
 * @date 2020/05/25 10:41
 */
class WelcomeActivity : BaseActivity<WelcomeModel, ViewDataBinding>() {
    var isFirst by Preference(Preference.IS_FIRST, true)
    val isLogin by Preference(Preference.IS_LOGIN, false)
    override fun layoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.getAppInfo()
        MyWebView
    }


    companion object{
        val APP_ID = "wx3a633c432181b7a8";
    }
    override fun initData() {
        // APP_ID 替换为你的应用从官方网站申请到的合法appID

// IWXAPI 是第三方app和微信通信的openApi接口
//        val api: IWXAPI;
//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
//
//        //建议动态监听微信启动广播进行注册到微信
//        registerReceiver(object :BroadcastReceiver(){
//            override fun onReceive(context: Context?, intent: Intent?) {
//                // 将该app注册到微信
//                api.registerApp(APP_ID);
//            }
//        },IntentFilter (ConstantsAPI.ACTION_REFRESH_WXAPP))
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            1 -> {
                if (isFirst) {
                    startActivity(Intent(this, GuideActivity::class.java))
                    isFirst = false
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }
            else -> {
                if (isFirst) {
                    startActivity(Intent(this, GuideActivity::class.java))
                    isFirst = false
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }

        }
    }
}