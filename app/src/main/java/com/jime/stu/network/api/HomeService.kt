package com.jime.stu.network.api

import com.jime.stu.bean.MyListBean
import com.jime.stu.bean.User
import com.jime.stu.app.base.BaseResult
import com.jime.stu.app.base.MyResult
import com.jime.stu.bean.Appinfo
import com.jime.stu.bean.ImageDetail
import com.jime.stu.network.entity.*
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 *   @auther : Aleyn
 *   time   : 2019/10/29
 */
interface HomeService {

    /**
     * 玩安卓轮播图
     */
    @GET("banner/json")
    suspend fun getBanner(): BaseResult<List<BannerBean>>


    /**
     * 导航数据
     */
    @GET("project/tree/json")
    suspend fun naviJson(): BaseResult<List<NavTypeBean>>


    /**
     * 项目列表
     * @param page 页码，从0开始
     */
    @GET("article/listproject/{page}/json")
    suspend fun getHomeList(@Path("page") page: Int): BaseResult<HomeListBean>


    /**
     * 项目列表
     * @param page 页码，从0开始
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int, @Query("cid") cid: Int): BaseResult<HomeListBean>


    /**
     * 常用网站
     */
    @GET("friend/json")
    suspend fun getPopularWeb(): BaseResult<List<UsedWeb>>

    /**
     * 常用网站
     */
    @GET("football/index")
    suspend fun getNewsList(@Query("page") page: Int,@Query("word") word: String?,@Query("key") key: String): News


    /**
     * 获取验证码
     */
    @POST("user/code")
    suspend fun getCode(@Query("phone") phone: String): MyResult<Any>

    /**
     * 登录
     */
    @POST("user/login")
    suspend fun login(@Query("udid") udid: String,@Query("phone") phone: String,@Query("code") code: String): MyResult<User>

    /**
     * 我的列表信息
     */
    @POST("user/myInfo")
    suspend fun myInfo(@Query("os") os: String,@Query("userId") userId: String): MyResult<MyListBean>

    /**
     * 意见反馈
     */
    @POST("lmessage/leaveMessage")
    suspend fun upFeelBack(@Query("userId") userId: String,@Query("udid") udid: String,@Query("content") content: String): MyResult<Any>

    /**
     * 本地文件上传
     */
    @POST("img/file")
    @Multipart
    suspend fun uploadFile(@Part imgFile: MultipartBody.Part): MyResult<ImageDetail>

    /**
     * 网络图片上传
     */
    @POST("/img/url")
    suspend fun uploadUrl(@Query("imgUrl") imgUrl: String): MyResult<ImageDetail>

    /**
     * 上传头像
     */
    @POST("user/icon")
    @Multipart
    suspend fun uploadHeadFile(@Part icon: MultipartBody.Part): MyResult<Any>

    /**
     * 启动接口
     */
    @POST("start/up")
    suspend fun getAppInfo(@Query("applicationId") applicationId: String,@Query("udid") udid: String
                           ,@Query("channel") channel: String,@Query("sysOs") sysOs: String
                           ,@Query("sysVersion") sysVersion: String,@Query("deviceBrand") deviceBrand: String
                           ,@Query("deviceModel") deviceModel: String,@Query("mac") mac: String
                           ,@Query("umengPid") umengPid: String): MyResult<Appinfo>
}