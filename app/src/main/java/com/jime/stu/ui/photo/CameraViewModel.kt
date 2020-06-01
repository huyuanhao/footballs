package com.jime.stu.ui.photo

import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.jime.stu.utils.InjectorUtil

/**
 * @author PC
 * @date 2020/06/01 18:05
 */
class CameraViewModel : BaseViewModel() {
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    fun uploadUrl(url:String) {
        launchOnlyresult(
            { homeRepository.uploadUrl(url) },
            {
                defUI.msgEvent.postValue(Message(1, obj = it))
            })
    }
}