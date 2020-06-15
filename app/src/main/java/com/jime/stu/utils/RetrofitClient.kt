package com.jime.stu.utils

import com.aleyn.mvvm.network.interceptor.Level
import com.aleyn.mvvm.network.interceptor.LoggingInterceptor
import com.blankj.utilcode.util.DeviceUtils
import com.jime.stu.BuildConfig
import com.jime.stu.common.Constant.BASE_URL
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 *   @auther : Aleyn
 *   time   : 2019/09/04
 */
class RetrofitClient {

    companion object {
        fun getInstance() =
            SingletonHolder.INSTANCE
        private lateinit var retrofit: Retrofit
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val SIGN_KEY = "&^&@#R&^*BDFBV389739843jlacv&%"
        val map = hashMapOf<String, String>("Accept-Encoding" to "identity")
        val u_udid = DeviceUtils.getAndroidID()
        var u_timestamp = System.currentTimeMillis()
        val sb = StringBuilder()
        sb.append("udid=").append(u_udid)
            .append("&timestamp=").append(u_timestamp)
            .append("&").append(SIGN_KEY)
//        var s = "udid=a08a065617dba455&timestamp=1590463248131&&^&@#R&^*BDFBV389739843jlacv&%"
        val u_sign: String = MD5Util.toMD5(sb.toString())

        map.put("u_udid", u_udid)
        map.put("u_timestamp", u_timestamp.toString())
        map.put("u_sign", u_sign)

        return OkHttpClient.Builder()
            .connectTimeout(20L, TimeUnit.SECONDS)
            .addNetworkInterceptor(LoggingInterceptor().apply {
                isDebug = BuildConfig.DEBUG
                level = Level.BASIC
                type = Platform.INFO
                requestTag = "Request"
                requestTag = "Response"
            })
            .writeTimeout(20L, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
            .addInterceptor(BaseInterceptor(map))
            .build()

    }

    fun <T> create(service: Class<T>?): T =
        retrofit.create(service!!) ?: throw RuntimeException("Api service is null!")
}