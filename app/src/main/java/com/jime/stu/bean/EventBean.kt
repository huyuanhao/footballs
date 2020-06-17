package com.jime.stu.bean

/**
 * @author PC
 * @date 2020/06/17 14:50
 */
data class EventBean(
    val udid: String,
    val userId: String,
    val url: String,
    val efrom: String,
    val etype: String,
    val title: String,
    val mode: String,
    val referer: String,
    val channel: String,
    val networktype: String,
    val systemname: String,
    val systemversion: String,
    val productversion: String,
    val mac: String,
    val imei: String,
    val idfa: String,
    val deviceBrand: String,
    val deviceModel: String,
    val androidid: String
)