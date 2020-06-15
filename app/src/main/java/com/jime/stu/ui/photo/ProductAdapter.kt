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
import com.jime.stu.databinding.ItemMeListBinding
import com.jime.stu.databinding.ItemRvXiangguan2Binding
import com.jime.stu.databinding.ItemUsedwebBinding

/**
 * @author PC
 * @date 2020/06/15 10:35
 */
class ProductAdapter:BaseQuickAdapter<Info,BaseDBViewHoder<ItemRvXiangguan2Binding>>(R.layout.item_rv_xiangguan2) {
    private val itemXiangguanOnClickListener = object : ProductModel.OnItemClickListener {
        override fun onItemClick(view: View, item: Info) {
            view.context.startActivity(
                Intent(view.context, WebActivity::class.java)
                    .putExtra("url", item.buyurl).putExtra("title", "相关产品")
            )
        }
    }

    override fun convert(helper: BaseDBViewHoder<ItemRvXiangguan2Binding>, item: Info?) {
        helper.getBinding().mInfo = item
        helper.getBinding().itemProdeuctListenner = itemXiangguanOnClickListener
        helper.getBinding().executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val binding = DataBindingUtil
            .inflate<ItemRvXiangguan2Binding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        }
    }
}