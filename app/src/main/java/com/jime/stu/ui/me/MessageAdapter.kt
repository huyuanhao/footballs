package com.jime.stu.ui.me

import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jime.stu.R
import com.jime.stu.bean.MesageBean
import com.jime.stu.utils.ImageLoaderManager
import com.scwang.smartrefresh.layout.util.DesignUtil

/**
 * @author PC
 * @date 2020/06/09 17:10
 */
class MessageAdapter: BaseQuickAdapter<MesageBean,BaseViewHolder>(R.layout.item_message) {
    override fun convert(helper: BaseViewHolder?, item: MesageBean?) {
        ImageLoaderManager.loadCircleImage(mContext,item?.title,helper?.getView(R.id.image))
        helper?.setText(R.id.tv_title,item?.title)
        helper?.setText(R.id.tv_detail,item?.text)
        helper?.setText(R.id.tv_time,item?.time)
    }

}