package com.jime.stu.ui.me

import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.LogUtils
import com.jime.stu.BR
import com.jime.stu.bean.MeInfo
import com.jime.stu.R
import com.jime.stu.network.entity.UsedWeb
import com.jime.stu.utils.InjectorUtil
import com.jime.stu.utils.Preference
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.io.File

/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
class MeViewModel : BaseViewModel() {
    var name = ObservableField<String>("未登录")
    var qianming = ObservableField<String>("登录获取更多服务")
    var imageDefault = ObservableField<Int>(R.mipmap.head_default)
    var imageUrl = ObservableField<String>("")
    var isLogin = ObservableField<Boolean>(false)//是否登录
    var is_member = ObservableField<Boolean>(false)//是否会员
    var vipDate = ObservableField<String>("解锁使用更多功能")
    var userName by Preference(Preference.USER_NAME, "")
    var headImg by Preference(Preference.HEADIMAGE, "")

    private var meItemOnClickListener = object : OnItemClickListener {
        override fun onItemClick(item: MeInfo) {
            defUI.msgEvent.postValue(Message(1, obj = item))
        }
    }
    var items = ObservableArrayList<MeInfo>()
    var itemBinding = ItemBinding.of<MeInfo>(BR.meBean, R.layout.item_me_list)
        .bindExtra(BR.item_meListenner, meItemOnClickListener)


    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    fun getItemList(){
        var token by Preference(Preference.TOKEN, "")
        if(TextUtils.isEmpty(token)) {
            items.clear()
            isLogin.set(false)
            name.set("未登录")
            qianming.set("登录获取更多服务")
            imageUrl.set("")
            is_member.set(false)
        }else{
            isLogin.set(true)
            launchOnlyresult({ homeRepository.myInfo()},{
                items.clear()
                items.addAll(it.dynamicList)
                name.set("已登录")
                imageUrl.set(headImg)
                qianming.set(userName)

                is_member.set(it.memberStatus)
            },isShowDialog = true)
        }
    }

    var popularWeb = MutableLiveData<List<UsedWeb>>()

    fun getPopularWeb() {
        launchGo({
            val result = homeRepository.getPopularWeb()
            if (result.isSuccess()) {
                popularWeb.value = result.data
            }
        })
    }
    fun uploadFile(file: File){
        launchOnlyresult({ homeRepository.uploadHeadFile(file) }, {
//            defUI.msgEvent.postValue(Message(1,obj = it))
            headImg = it.toString()
            imageUrl.set(headImg)
        })
    }

    fun save(url:String,etypeInt:Int,title:String,mode:String){
        launchOnlyresult({homeRepository.save(url,etypeInt,title,mode)},{
            LogUtils.e("事件上报成功：url="+ url +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{
            LogUtils.e("事件上报失败：url="+ url +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{},false)
    }

    interface OnItemClickListener {
        fun onItemClick(item: MeInfo)
    }
}