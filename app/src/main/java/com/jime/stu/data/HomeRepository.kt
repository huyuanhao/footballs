package com.jime.stu.data

import com.aleyn.mvvm.base.BaseModel
import com.blankj.utilcode.util.DeviceUtils
import com.jime.stu.Bean.User
import com.jime.stu.app.base.BaseResult
import com.jime.stu.app.base.MyResult
import com.jime.stu.data.db.dao.HomeDao
import com.jime.stu.data.http.HomeNetWork
import com.jime.stu.network.entity.*
import com.jime.stu.utils.Preference

/**
 *   @auther : Aleyn
 *   time   : 2019/10/29
 */
class HomeRepository private constructor(
    private val netWork: HomeNetWork,
    private val localData: HomeDao
) : BaseModel() {

    suspend fun getBannerData(refresh: Boolean = false): List<BannerBean> {
        return cacheNetCall({
            netWork.getBannerData()
        }, {
            localData.getBannerList()
        }, {
            if (refresh) localData.deleteBannerAll()
            localData.insertBanner(it)
        }, {
            !refresh && it != null && it.isNotEmpty()
        })
    }

    suspend fun getHomeList(page: Int, refresh: Boolean): HomeListBean {
        return cacheNetCall({
            netWork.getHomeList(page)
        }, {
            localData.getHomeList(page + 1)
        }, {
            if (refresh) localData.deleteHomeAll()
            localData.insertData(it)
        }, {
            !refresh && it != null
        })
    }

    suspend fun getNaviJson(): BaseResult<List<NavTypeBean>> {
        return netWork.getNaviJson()
    }

    suspend fun getProjectList(page: Int, cid: Int): BaseResult<HomeListBean> {
        return netWork.getProjectList(page, cid)
    }

    suspend fun getPopularWeb(): BaseResult<List<UsedWeb>> {
        return netWork.getPopularWeb()
    }

    suspend fun getNewsList(page: Int, word: String?, refresh: Boolean): News {
        var s = word
        if("全部".equals(word)){
            s = null
        }
        return netWork.getNewsList(page, s)
    }

    //获取验证码
    suspend fun getCode(phone: String): MyResult<Any> {
        return netWork.getCode(phone)
    }

    //获取验证码
    suspend fun login(phone: String,code:String): MyResult<User> {
        val udid = DeviceUtils.getAndroidID()
        return netWork.login(udid,phone,code)
    }

    companion object {

        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(netWork: HomeNetWork, homeDao: HomeDao) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(netWork, homeDao).also { INSTANCE = it }
            }
    }
}