package com.jime.stu.utils

import androidx.databinding.BindingAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * @author PC
 * @date 2020/05/07 17:25
 */
object MyAdapter {
    @BindingAdapter(value = ["android:setFresh"], requireAll = false)
    @JvmStatic
    fun setFresh(freshLayout: SmartRefreshLayout, listener: OnRefreshLoadMoreListener) {
        freshLayout.setOnRefreshLoadMoreListener(listener)
    }
}