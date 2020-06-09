package com.jime.stu.ui.me

import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.utils.InjectorUtil

/**
 * @author PC
 * @date 2020/06/09 11:19
 */
class FeelBackModel : BaseViewModel() {
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    fun upFeel(title: String) {
        launchOnlyresult({
            homeRepository.upFeelBack(title)
        }, {
            ToastUtils.showShort("上传成功")
            defUI.msgEvent.postValue(Message(1))
        })
    }
}