package com.jime.stu.ui.photo

import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.jime.stu.utils.InjectorUtil
import java.io.File

/**
 * @author PC
 * @date 2020/06/15 10:12
 */
class HistoryViewModel :BaseViewModel(){
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    fun toSearch(file: File){
        launchOnlyresult({ homeRepository.uploadFile(file) }, {
            it.imgUrl = file.absolutePath
            defUI.msgEvent.postValue(Message(1,obj = it))
        },{
            defUI.msgEvent.postValue(Message(it.code,obj = it.errMsg))
        },{},true,"识别中")
    }
}