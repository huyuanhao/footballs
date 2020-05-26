package com.jime.stu.utils

import com.jime.stu.data.HomeRepository
import com.jime.stu.data.db.LinDatabase
import com.jime.stu.data.http.HomeNetWork

object InjectorUtil {

    fun getHomeRepository() = HomeRepository.getInstance(
        HomeNetWork.getInstance(),
        LinDatabase.getInstanse().homeLocaData()
    )

}