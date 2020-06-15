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
import com.jime.stu.databinding.ItemRvRelated2Binding
import com.jime.stu.databinding.ItemRvXiangguan2Binding
import com.jime.stu.databinding.ItemUsedwebBinding

/**
 * @author PC
 * @date 2020/06/15 10:35
 */
class SimipicAdapter:BaseQuickAdapter<String,BaseDBViewHoder<ItemRvRelated2Binding>>(R.layout.item_rv_related2) {
    private val itemRelatedOnClickListener = object : SimipicModel.OnItemClickListener {
        override fun onItemClick(view: View, item: String) {
            view.context.startActivity(Intent(view.context, PictureDetailActivity::class.java)
                .putExtra("simipic", data as ArrayList)
                .putExtra("position", data.indexOf(item)))
        }
    }

    override fun convert(helper: BaseDBViewHoder<ItemRvRelated2Binding>, item: String?) {
        helper.getBinding().mSimipic = item
        helper.getBinding().itemRelatedListenner = itemRelatedOnClickListener
        helper.getBinding().executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val binding = DataBindingUtil
            .inflate<ItemRvRelated2Binding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        }
    }
}