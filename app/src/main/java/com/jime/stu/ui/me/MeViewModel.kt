package com.jime.stu.ui.me

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.jime.stu.network.entity.UsedWeb
import com.jime.stu.utils.InjectorUtil

/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
class MeViewModel : BaseViewModel() {
    var name = ObservableField<String>("昵称")
    var qianming = ObservableField<String>("个性签名")
    var imageUrl = ObservableField<String>("")
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    var popularWeb = MutableLiveData<List<UsedWeb>>()

    fun getPopularWeb() {
        launchGo({
            val result = homeRepository.getPopularWeb()
            if (result.isSuccess()) {
                popularWeb.value = result.data
            }
        })
    }
}