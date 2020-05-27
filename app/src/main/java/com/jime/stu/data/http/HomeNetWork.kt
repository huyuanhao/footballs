package com.jime.stu.data.http

import com.jime.stu.network.api.HomeService
import com.jime.stu.utils.RetrofitClient

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
class HomeNetWork {

    private val mService by lazy { RetrofitClient.getInstance().create(HomeService::class.java) }

    suspend fun getBannerData() = mService.getBanner()

    suspend fun getHomeList(page: Int) = mService.getHomeList(page)

    suspend fun getNaviJson() = mService.naviJson()

    suspend fun getProjectList(page: Int, cid: Int) = mService.getProjectList(page, cid)

    suspend fun getPopularWeb() = mService.getPopularWeb()

    suspend fun getNewsList(page: Int,word:String?) = mService.getNewsList(page,word,"bcdbd7c08ca1c1e30364282c95ec2b07")

    //获取验证码
    suspend fun getCode(phone: String) = mService.getCode(phone)

    //登录
    suspend fun login(udid:String,phone: String,code:String) = mService.login(udid,phone,code)


    //我的列表信息
    suspend fun myInfo(os:String,userId: String) = mService.myInfo(os,userId)

    companion object {
        @Volatile
        private var netWork: HomeNetWork? = null

        fun getInstance() = netWork ?: synchronized(this) {
            netWork ?: HomeNetWork().also { netWork = it }
        }
    }


}