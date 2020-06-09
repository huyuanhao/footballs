package com.jime.stu.app

import android.content.Context
import android.util.Log
import com.aleyn.mvvm.base.BaseApplication
import com.blankj.utilcode.util.LogUtils
import com.jime.stu.BuildConfig
import com.jime.stu.R
import com.jime.stu.bean.User
import com.jime.stu.utils.Preference
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage
import com.umeng.socialize.PlatformConfig
import kotlin.properties.Delegates


/**
 *   @auther : Aleyn
 *   time   : 2019/11/04
 */
class MyApplication : BaseApplication() {
    var TAG = "MyApplication"
    companion object{
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.white);//全局设置主题颜色
                ClassicsHeader(context) as RefreshHeader;//.setTi
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator() { context, layout ->
                ClassicsFooter(context).setDrawableSize(20f);
            }
        }
        var CONTEXT: Context by Delegates.notNull()
        lateinit var USER: User
    }
    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
//        SmartCropper.buildImageDetector(this);
        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
// 参数一：当前上下文context；
// 参数二：应用申请的Appkey（需替换）；
// 参数三：渠道名称；
// 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
// 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, "5ecb8140895cca850100025b", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "e9f97f44a488ab91ab2fb089f62aef0d")
        PlatformConfig.setWeixin("wx3a633c432181b7a8", "279a985b623a404a415d64eac217359f")
        //获取消息推送代理示例
        //获取消息推送代理示例
        val mPushAgent = PushAgent.getInstance(this)
//注册推送服务，每次调用register方法都会回调该接口
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String) { //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG, "注册成功：deviceToken：-------->  $deviceToken")
                var UMENGPID by Preference(Preference.UMENGPID, "")
                UMENGPID = deviceToken
            }

            override fun onFailure(s: String, s1: String) {
                Log.e(TAG, "注册失败：-------->  s:$s,s1:$s1")
            }
        })
        val notificationClickHandler: UmengNotificationClickHandler =
            object : UmengNotificationClickHandler() {
                override fun dealWithCustomAction(
                    context: Context,
                    msg: UMessage
                ) {
                    LogUtils.e("umessage" + msg.toString())
                }

                override fun openActivity(context: Context?, msg: UMessage?) {
                    super.openActivity(context, msg);
                    LogUtils.e("umessage + openActivity" + msg.toString())
                }
            }
        mPushAgent.notificationClickHandler = notificationClickHandler

        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }

    }
}