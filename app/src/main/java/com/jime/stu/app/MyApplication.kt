package com.jime.stu.app

import android.content.Context
import com.aleyn.mvvm.base.BaseApplication
import com.blankj.utilcode.util.LogUtils
import com.jime.stu.bean.User
import com.jime.stu.BuildConfig
import com.jime.stu.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlin.properties.Delegates

/**
 *   @auther : Aleyn
 *   time   : 2019/11/04
 */
class MyApplication : BaseApplication() {
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
        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }
    }
}