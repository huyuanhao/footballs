package com.jime.stu.ui.photo

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.jime.stu.BR
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.bean.Info
import com.jime.stu.utils.InjectorUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author PC
 * @date 2020/06/02 09:14
 */
class SimipicModel :BaseViewModel(){
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    private val itemRelatedOnClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, item: String) {
            view.context.startActivity(Intent(view.context, PictureDetailActivity::class.java)
                .putExtra("simipic", items)
                .putExtra("position", items.indexOf(item)))
        }
    }

    var items = ObservableArrayList<String>()
    var itemBinding = ItemBinding.of<String>(BR.mSimipic, R.layout.item_rv_related2)
        .bindExtra(BR.item_relatedListenner, itemRelatedOnClickListener)

    interface OnItemClickListener {
        fun onItemClick(view: View, item: String)
    }
}