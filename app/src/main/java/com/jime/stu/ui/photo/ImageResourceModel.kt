package com.jime.stu.ui.photo

import androidx.databinding.ObservableArrayList
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.jime.stu.BR
import com.jime.stu.R
import com.jime.stu.bean.Info
import com.jime.stu.bean.SameInfo
import com.jime.stu.utils.InjectorUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author PC
 * @date 2020/06/02 09:14
 */
class ImageResourceModel :BaseViewModel(){
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    private val itemSourceOnClickListener = object : OnItemSourceClickListener {
        override fun onItemClick(item: SameInfo) {
            defUI.msgEvent.postValue(Message(0, obj = item))
        }
    }

    var itemsSource = ObservableArrayList<SameInfo>()
    var itemSourceBinding = ItemBinding.of<SameInfo>(BR.mSameInfo, R.layout.item_rv_source2)
        .bindExtra(BR.item_source2Listenner, itemSourceOnClickListener)

    interface OnItemSourceClickListener {
        fun onItemClick(item: SameInfo)
    }
}