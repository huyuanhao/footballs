package com.jime.stu.ui.photo

import androidx.databinding.ObservableArrayList
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.jime.stu.BR
import com.jime.stu.R
import com.jime.stu.bean.Info
import com.jime.stu.utils.InjectorUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author PC
 * @date 2020/06/02 09:14
 */
class ProductModel :BaseViewModel(){
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    private val itemXiangguanOnClickListener = object : OnItemClickListener {
        override fun onItemClick(item: Info) {
            defUI.msgEvent.postValue(Message(0, obj = item))
        }
    }

    var items = ObservableArrayList<Info>()
    var itemBinding = ItemBinding.of<Info>(BR.mInfo, R.layout.item_rv_xiangguan2)
        .bindExtra(BR.item_prodeuctListenner, itemXiangguanOnClickListener)

    interface OnItemClickListener {
        fun onItemClick(item: Info)
    }
}