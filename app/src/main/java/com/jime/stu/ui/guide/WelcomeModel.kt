package com.jime.stu.ui.guide

import androidx.databinding.ObservableArrayList
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.LogUtils
import com.jime.stu.BR
import com.jime.stu.R
import com.jime.stu.bean.Info
import com.jime.stu.utils.InjectorUtil
import com.jime.stu.utils.Preference
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author PC
 * @date 2020/06/02 09:14
 */
class WelcomeModel :BaseViewModel(){
    var userAgree by Preference(Preference.USERAGREE, "https://photoapi.jimetec.com/shitu/dist/agreement/userAgree.html")
    var paymentAgree by Preference(Preference.PAYMENTAGREE, "https://photoapi.jimetec.com/shitu/dist/agreement/memberAgree.html")
    var unlockurl by Preference(Preference.UNLOCKURL, "https://photoapi.jimetec.com/shitu/dist/pay.html")
    var shareContent by Preference(Preference.SHARECONTENT, "万能识图-一款基于人工智能的识别软件，如：(电影/公众)人物明星、花草、电子产品、食物、汽车化妆品等等。只需拍照或者一张图片，我们即可帮您认识TA。并且能提供百科介绍和直接购买地址。赶紧试试吧")
    var privateAgree by Preference(Preference.PRIVATEAGREE, "https://photoapi.jimetec.com/shitu/dist/agreement/privateAgree.html")
    var shareTitle by Preference(Preference.SHARETITLE, "身边万物，拍照即可识别")
    var downloadUrl by Preference(Preference.DOWNLOADURL, "https://www.baidu.com")
    var shareUrl by Preference(Preference.SHAREURL, "https://photoapi.jimetec.com/shitu/stu-share/index.html")
    var appDomainUrl by Preference(Preference.APPDOMAINURL, "https://photoapi.jimetec.com/")
    var versionCode by Preference(Preference.VERSIONCODE, "101")
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    interface OnItemClickListener {
        fun onItemClick(item: Info)
    }

    fun getAppInfo(){
        launchOnlyresult({ homeRepository.getAppInfo() }, {
            defUI.msgEvent.postValue(Message(1,obj = it))
            LogUtils.e("appinfo"+it.toString())
            userAgree = it.dataDictionary.userAgree
            paymentAgree = it.dataDictionary.paymentAgree
            unlockurl = it.dataDictionary.unlockurl
            shareContent = it.dataDictionary.shareContent
            privateAgree = it.dataDictionary.privateAgree
            shareTitle = it.dataDictionary.shareTitle
            downloadUrl = it.dataDictionary.downloadUrl
            shareUrl = it.dataDictionary.shareUrl
            appDomainUrl = it.dataDictionary.appDomainUrl
            versionCode = it.dataDictionary.versionCode
        },isShowDialog = false)
    }
}