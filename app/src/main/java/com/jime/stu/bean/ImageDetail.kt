package com.jime.stu.bean

/**
 * @author PC
 * @date 2020/05/28 17:58
 */
data class ImageDetail(
    val baike: Baike,
    val cardHeader: CardHeader,
    val imgName: String,
    val product: Product,
    val same: Same,
    val simipic: Simipic
)


data class Baike(
    val desc: String,
    val imgUrl: String,
    val moreUrl: String,
    val text: String
)


data class CardHeader(
    val imageUrl: String,
    val maybeName: String
)

data class Product(
    val listInfo: List<Info>,
    val title: String
)

data class Same(
    val listSameInfo: List<SameInfo>,
    val title: String
)

data class Simipic(
    val imageUrl: String,
    val listThumbUrl: List<String>,
    val title: String
)

data class Info(
    val buyurl: String,
    val desc: String,
    val imgUrl: String,
    val source: String,
    val text: String
)

data class SameInfo(
    val abstractDesc: String,
    val imageSrc: String,
    val titleDesc: String,
    val url: String,
    val website: String
)