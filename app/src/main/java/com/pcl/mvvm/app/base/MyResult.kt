package com.pcl.mvvm.app.base

import com.aleyn.mvvm.base.IBaseResponse

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
data class MyResult<T>(
    val msg: String,
    val code: Int,
    val data: T

) : IBaseResponse<T> {

    override fun code() = code

    override fun msg() = msg

    override fun data() = data

    override fun isSuccess() = code == 200

}