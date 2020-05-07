package com.pcl.mvvm.app

import android.content.Context
import com.aleyn.mvvm.base.BaseApplication
import com.blankj.utilcode.util.LogUtils
import com.pcl.mvvm.BuildConfig
import com.pcl.mvvm.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

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
    }
    override fun onCreate() {
        super.onCreate()
        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }
    }
}