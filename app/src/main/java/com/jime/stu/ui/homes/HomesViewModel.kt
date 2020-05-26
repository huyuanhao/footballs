package com.jime.stu.ui.homes

import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.jime.stu.network.entity.BannerBean
import com.jime.stu.network.entity.HomeListBean
import com.jime.stu.utils.InjectorUtil

class HomesViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    private val mBanners = MutableLiveData<List<BannerBean>>()

    private val projectData = MutableLiveData<HomeListBean>()

    fun getBanner(refresh: Boolean = false): MutableLiveData<List<BannerBean>> {
        launchGo({
            mBanners.value = homeRepository.getBannerData(refresh)
        })
        return mBanners
    }

    /**
     * @param page 页码
     * @param refresh 是否刷新
     */
    fun getHomeList(page: Int, refresh: Boolean = false): MutableLiveData<HomeListBean> {
        launchGo({
            projectData.value = homeRepository.getHomeList(page, refresh)
        })
        return projectData
    }
}
