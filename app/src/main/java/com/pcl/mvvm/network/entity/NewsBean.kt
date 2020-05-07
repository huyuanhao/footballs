package com.pcl.mvvm.network.entity

/**
 * @author PC
 * @date 2020/05/07 10:46
 */
data class NewsBean(
    val ctime: String,
    val description: String,
    val picUrl: String,
    val title: String,
    val url: String
)

data class News(
    val msg: String,
    val code: Int,
    val newslist: List<NewsBean>
)