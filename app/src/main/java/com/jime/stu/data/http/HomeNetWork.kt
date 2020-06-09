package com.jime.stu.data.http

import com.blankj.utilcode.util.DeviceUtils
import com.jime.stu.BuildConfig
import com.jime.stu.app.base.MyResult
import com.jime.stu.bean.Appinfo
import com.jime.stu.network.api.HomeService
import com.jime.stu.utils.Preference
import com.jime.stu.utils.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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

    //启动接口
    suspend fun getAppInfo(): MyResult<Appinfo> {
        val applicationId = BuildConfig.APPLICATION_ID
        val udid = DeviceUtils.getAndroidID()
        val channel = BuildConfig.channel
        val sysOs = "android"
        val sysVersion = BuildConfig.VERSION_CODE.toString()
        val deviceBrand = DeviceUtils.getManufacturer()
        val deviceModel = DeviceUtils.getModel()
        val mac = DeviceUtils.getMacAddress()
        val umengpid by Preference(Preference.UMENGPID, "")
        return mService.getAppInfo(applicationId, udid,channel,sysOs,
            sysVersion,deviceBrand,deviceModel,mac,umengpid)
    }

    //获取验证码
    suspend fun getCode(phone: String) = mService.getCode(phone)

    //登录
    suspend fun login(udid:String,phone: String,code:String) = mService.login(udid,phone,code)


    //我的列表信息
    suspend fun myInfo(os:String,userId: String) = mService.myInfo(os,userId)

    //我的列表信息
    suspend fun upFeelBack(content:String):  MyResult<Any> {
        val userId by Preference(Preference.USER_ID, "")
        val udid = DeviceUtils.getAndroidID()
        return mService.upFeelBack(userId,udid,content)
    }

    //本地文件上传
    suspend fun uploadFile(file:File) = mService.uploadFile(
        MultipartBody.Part.createFormData(
            "imgFile", file.name, RequestBody.create(
                "image/png".toMediaTypeOrNull(), file
            )
        ))

    //网络图片上传
    suspend fun uploadUrl(imgUrl: String) = mService.uploadUrl(imgUrl)

    companion object {
        @Volatile
        private var netWork: HomeNetWork? = null

        fun getInstance() = netWork ?: synchronized(this) {
            netWork ?: HomeNetWork().also { netWork = it }
        }
    }


}