package com.jime.stu.ui.photo

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.LogUtils
import com.jime.stu.BR
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.bean.Info
import com.jime.stu.network.entity.UsedWeb
import com.jime.stu.utils.InjectorUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @author PC
 * @date 2020/06/02 09:14
 */
class ProductModel : BaseViewModel() {
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    private val itemXiangguanOnClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, item: Info) {
            save(item.buyurl, 1, "跳转的HTML页面", "跳转的HTML页面")
            view.context.startActivity(
                Intent(
                    view.context, WebActivity::class.java
                )
                    .putExtra("url", item.buyurl).putExtra("title", "相关产品")
            )
        }
    }
    var mList = MutableLiveData<List<Info>>()
    //    var items = ObservableArrayList<Info>()
//    var itemBinding = ItemBinding.of<Info>(BR.mInfo, R.layout.item_rv_xiangguan2)
//        .bindExtra(BR.item_prodeuctListenner, itemXiangguanOnClickListener)
    fun save(url: String, etypeInt: Int, title: String, mode: String) {
        launchOnlyresult({ homeRepository.save(url, etypeInt, title, mode) }, {
            LogUtils.e("事件上报成功：url=" + url + "etypeInt=" + etypeInt + "title=" + title + "mode=" + mode)
        }, {
            LogUtils.e("事件上报失败：url=" + url + "etypeInt=" + etypeInt + "title=" + title + "mode=" + mode)
        }, {}, false)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, item: Info)
    }
}