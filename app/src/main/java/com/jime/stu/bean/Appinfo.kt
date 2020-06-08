package com.jime.stu.bean

/**
 * @author PC
 * @date 2020/06/08 14:38
 */
data class Appinfo(
    val applicationId: String,
    val dataDictionary: DataDictionary
)

//appDomainUrl  接口请求域名
//downloadUrl   apk下载地址
//versionCode   版本号
//userAgree     用户协议
//privateAgree  隐私协议
//paymentAgree  付费会员协议
//unlockurl     h5支付跳转页面
//shareTitle    分享标题
//shareContent  分享内容
//shareUrl      分享落地页
data class DataDictionary(
    val userAgree:String =  "https://photoapi.jimetec.com/shitu/dist/agreement/userAgree.html",
    val paymentAgree: String = "https://photoapi.jimetec.com/shitu/dist/agreement/memberAgree.html",
    val unlockurl: String = "https://photoapi.jimetec.com/shitu/dist/pay.html",
    val shareContent: String = "万能识图-一款基于人工智能的识别软件，如：(电影/公众)人物明星、花草、电子产品、食物、汽车化妆品等等。只需拍照或者一张图片，我们即可帮您认识TA。并且能提供百科介绍和直接购买地址。赶紧试试吧",
    val privateAgree: String = "https://photoapi.jimetec.com/shitu/dist/agreement/privateAgree.html",
    val shareTitle: String = "身边万物，拍照即可识别",
    val downloadUrl: String = "https://www.baidu.com",
    val shareUrl: String = "https://photoapi.jimetec.com/shitu/stu-share/index.html",
    val appDomainUrl: String = "https://photoapi.jimetec.com/",
    val versionCode: String = "101"
)