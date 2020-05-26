package com.jime.stu.Bean

/**
 * @author PC
 * @date 2020/05/25 10:53
 */

data class User(
    val headImage: String,
    val memberStatus: Boolean,
    val token: String,
    val udid: String,
    val userId: Int,
    val userName: String
)