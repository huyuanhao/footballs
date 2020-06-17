package com.jime.stu.data.http

import com.blankj.utilcode.util.*
import com.jime.stu.BuildConfig
import com.jime.stu.app.MyApplication
import com.jime.stu.app.base.MyResult
import com.jime.stu.bean.Appinfo
import com.jime.stu.bean.EventBean
import com.jime.stu.network.api.HomeService
import com.jime.stu.utils.AppUtils
import com.jime.stu.utils.Preference
import com.jime.stu.utils.RetrofitClient
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
class HomeNetWork {
    var userId by Preference(Preference.USER_ID, "")

    private val mService by lazy { RetrofitClient.getInstance().create(HomeService::class.java) }

    suspend fun getBannerData() = mService.getBanner()

    suspend fun getHomeList(page: Int) = mService.getHomeList(page)

    suspend fun getNaviJson() = mService.naviJson()

    suspend fun getProjectList(page: Int, cid: Int) = mService.getProjectList(page, cid)

    suspend fun getPopularWeb() = mService.getPopularWeb()

    suspend fun getNewsList(page: Int, word: String?) =
        mService.getNewsList(page, word, "bcdbd7c08ca1c1e30364282c95ec2b07")

    //启动接口
    var UMENGPID by Preference(Preference.UMENGPID, "")
    suspend fun getAppInfo(): MyResult<Appinfo> {
        val applicationId = BuildConfig.APPLICATION_ID
        val androidid = DeviceUtils.getAndroidID()
        val udid = DeviceUtils.getAndroidID()
        var ime = AppUtils.getIMEI(MyApplication.CONTEXT)
        val channel = BuildConfig.channel
        val sysOs = "android"
        val sysVersion = DeviceUtils.getSDKVersionName()
        val deviceBrand = DeviceUtils.getManufacturer()
        val deviceModel = DeviceUtils.getModel()
        val mac = DeviceUtils.getMacAddress()
//        val umengpid by Preference(Preference.UMENGPID, "")
        val umengpid by Preference(Preference.UMENGPID, "")

        if(ime == null){
            ime = ""
        }
        LogUtils.e(
            "getAppInfo",
            "applicationId+" + applicationId +
                    " androidid=" + androidid + " udid=" + udid + " ime=" + ime + " channel=" + channel + " sysOs=" + sysOs + " sysVersion=" + sysVersion + " deviceBrand="
                    + deviceBrand + " deviceModel=" + deviceModel + " mac=" + mac + " umengpid=" + umengpid
        )
        return mService.getAppInfo(
            applicationId, udid, androidid, ime, channel, sysOs,
            sysVersion, deviceBrand, deviceModel, mac, umengpid
        )
    }

    //获取验证码
    suspend fun getCode(phone: String) = mService.getCode(phone)

    //登录
    suspend fun login(udid: String, phone: String, code: String) = mService.login(udid, phone, code)


    //我的列表信息
    suspend fun myInfo(os: String, userId: String) = mService.myInfo(os, userId)

    //意见反馈
    suspend fun upFeelBack(content: String): MyResult<Any> {
        val userId by Preference(Preference.USER_ID, "")
        val udid = DeviceUtils.getAndroidID()
        return mService.upFeelBack(userId, udid, content)
    }

    //本地文件上传
    suspend fun uploadFile(file: File) = mService.uploadFile(
        MultipartBody.Part.createFormData(
            "imgFile", file.name, RequestBody.create(
                "image/png".toMediaTypeOrNull(), file
            )
        )
    )

    //网络图片上传
    suspend fun uploadUrl(imgUrl: String) = mService.uploadUrl(imgUrl)

    //反馈错误
    suspend fun upError(imgUrl: String, data: String) = mService.upError(imgUrl, data)

    //头像上传
    suspend fun uploadHeadFile(file: File) = mService.uploadHeadFile(
        MultipartBody.Part.createFormData(
            "icon", file.name, RequestBody.create(
                "image/png".toMediaTypeOrNull(), file
            )
        )
    )

    //事件上传
    suspend fun save(url: String, etypeInt: Int, title: String, mode: String): MyResult<Any> {
        var etype: String
        if (etypeInt == 0) {
            etype = "view"
        } else {
            etype = "click"
        }
        var dataBean = EventBean(
            udid = DeviceUtils.getAndroidID(),
            userId = userId,
            url = url,
            efrom = "shitu",
            etype = etype,
            title = title,
            mode = mode,
            referer = "",
            channel = BuildConfig.channel,
            networktype = NetworkUtils.getNetworkType().name,
            systemname = "android",
            systemversion = DeviceUtils.getSDKVersionName(),
            productversion = BuildConfig.VERSION_CODE.toString(),
            mac = DeviceUtils.getMacAddress(),
            imei = AppUtils.getIMEI(MyApplication.CONTEXT),
            idfa = "",
            deviceBrand = DeviceUtils.getManufacturer(),
            deviceModel = DeviceUtils.getModel(),
            androidId = DeviceUtils.getAndroidID()
        )
        var data = "";
        try {
            data = GsonUtils.toJson(dataBean)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogUtils.e("事件上报：" + data)
        var body = data.toRequestBody("application/json;charset=UTF-8".toMediaType())
        return mService.save(body)
//       return mService.save(RequestBody.create("application/json;charset=UTF-8".toMediaType(), data))
    }

    companion object {
        @Volatile
        private var netWork: HomeNetWork? = null

        fun getInstance() = netWork ?: synchronized(this) {
            netWork ?: HomeNetWork().also { netWork = it }
        }
    }


}