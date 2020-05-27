package com.jime.stu.ui.me

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.jime.stu.BR
import com.jime.stu.bean.MeInfo
import com.jime.stu.R
import com.jime.stu.network.entity.UsedWeb
import com.jime.stu.utils.InjectorUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
class MeViewModel : BaseViewModel() {
    var name = ObservableField<String>("昵称")
    var qianming = ObservableField<String>("个性签名")
    var imageUrl = ObservableField<String>("")
    var is_member = ObservableField<Boolean>(false)
    var vipDate = ObservableField<String>("解锁使用更多功能")

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
        launchOnlyresult({ homeRepository.myInfo()},{
            items.clear()
            items.addAll(it.dynamicList)
        })
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

    interface OnItemClickListener {
        fun onItemClick(item: MeInfo)
    }
}