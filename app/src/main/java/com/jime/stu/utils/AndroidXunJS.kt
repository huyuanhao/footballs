package com.jime.stu.utils

import android.app.Activity
import android.content.Intent
import android.webkit.JavascriptInterface
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.PhoneUtils
import com.jime.stu.BuildConfig
import com.jime.stu.app.MyApplication
import com.jime.stu.bean.EventJson
import com.jime.stu.ui.MainActivity
import com.jime.stu.ui.login.LoginActivity
import java.util.*

/**
 * @author PC
 * @date 2020/06/08 18:05
 */
class AndroidXunJS(//            userJson = UserUtil.getUserJson();
    var mActivity: Activity
) {
    var phone by Preference(Preference.PHONE, "")
    var userId by Preference(Preference.USER_ID, "")

    //获取用户信息
    @get:JavascriptInterface
    val userInfo: String?
        get() {
            val userJson: String? = null
            try { //            userJson = UserUtil.getUserJson();
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return userJson
        }

    //PhoneUtils.getIMEI()
    @JavascriptInterface
    fun getEventData(): String? {
        var event_json: String? = null
        val event = EventJson(
            phone = phone,
            udid = DeviceUtils.getAndroidID(),
            userId = userId,
            systemname = "h5",
            url = "",
            efrom = "支付",
            etype = "click",
            title = "支付",
            mode = "首頁",
            productId = "",
            referer = "",
            channel = BuildConfig.channel,
            networktype = NetworkUtils.getNetworkType().name,
            systemversion = DeviceUtils.getSDKVersionName(),
            productversion = BuildConfig.VERSION_CODE.toString(),
            eventTime = Date().getTime().toString(),
            power = "",
            gyo = "",
            batterystatus = "",
            batteryType = "",
            refererUrl = "",
            mac = DeviceUtils.getMacAddress(),
            imei = AppUtils.getIMEI(MyApplication.CONTEXT),
            idfa = "",
            androidid = DeviceUtils.getAndroidID(),
            deviceBrand = DeviceUtils.getManufacturer(),
            deviceModel = DeviceUtils.getModel(),
            applicationid = BuildConfig.APPLICATION_ID
        )
        try {
            event_json = GsonUtils.toJson(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return event_json
    }

    //去主页
    @JavascriptInterface
    fun toMainPage() {
        try {
            mActivity.startActivity(Intent(mActivity, MainActivity::class.java))
            mActivity.finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JavascriptInterface
    fun toLoginPage() {
        try {
            val intent =
                Intent(mActivity, LoginActivity::class.java)
            mActivity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //    @JavascriptInterface
//    public void toWebUser() {
//        MyWebViewActivity.startTo(mActivity, Constants.AGREEMENT_URL_USER, "用户协议", "用户协议");
//    }
//
//
//    @JavascriptInterface
//    public void toWebCost() {
//        String useUrl = "";
//        MyWebViewActivity.startTo(mActivity, Constants.LOGIN_URL_COST, "付费会员服务协议", "付费会员服务协议");
//    }
//位置信息
    @JavascriptInterface
    fun closePage() {
        try {
            mActivity.finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}