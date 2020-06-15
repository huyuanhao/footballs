package com.jime.stu.ui.photo

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.app.base.BaseDBViewHoder
import com.jime.stu.bean.Info
import com.jime.stu.bean.SameInfo
import com.jime.stu.databinding.*

/**
 * @author PC
 * @date 2020/06/15 10:35
 */
class ImageResourceAdapter:BaseQuickAdapter<SameInfo,BaseDBViewHoder<ItemRvSource2Binding>>(R.layout.item_rv_source2) {
    private val itemSourceOnClickListener = object : ImageResourceModel.OnItemSourceClickListener {
        override fun onItemClick(view: View, item: SameInfo) {
            view.context.startActivity(
                Intent(view.context, WebActivity::class.java)
                    .putExtra("url", item.url).putExtra("title", "图片来源")
            )
        }
    }

    override fun convert(helper: BaseDBViewHoder<ItemRvSource2Binding>, item: SameInfo?) {
        helper.getBinding().mSameInfo = item
        helper.getBinding().itemSource2Listenner = itemSourceOnClickListener
        helper.getBinding().executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val binding = DataBindingUtil
            .inflate<ItemRvSource2Binding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        }
    }
}