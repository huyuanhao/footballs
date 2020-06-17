package com.jime.stu

import com.aleyn.mvvm.base.BaseViewModel
import com.blankj.utilcode.util.LogUtils
import com.jime.stu.utils.InjectorUtil

/**
 * @author PC
 * @date 2020/06/17 15:54
 */
class MainViewModel : BaseViewModel(){
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    fun save(url:String,etypeInt:Int,title:String,mode:String){
        launchOnlyresult({homeRepository.save(url,etypeInt,title,mode)},{
            LogUtils.e("事件上报成功：url="+ url  +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{
            LogUtils.e("事件上报失败：url="+ url  +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{},false)
    }
}