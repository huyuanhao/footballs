package com.pcl.mvvm.ui.homes

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pcl.mvvm.R
import com.pcl.mvvm.network.entity.NewsBean
import com.pcl.mvvm.utils.ImageLoaderManager

/**
 *   @auther : Aleyn
 *   time   : 2019/11/08
 */
class NewsListAdapter : BaseQuickAdapter<NewsBean, BaseViewHolder>(R.layout.item_news_list) {


    override fun convert(helper: BaseViewHolder?, item: NewsBean?) {
        with(helper!!) {
            setText(R.id.tv_project_list_atticle_type, item!!.ctime)
            setText(R.id.tv_project_list_atticle_title, item.title)
            setText(R.id.tv_project_list_atticle_time, item.ctime)
            setText(R.id.tv_project_list_atticle_auther, item.description)
            val imageView = helper.getView<ImageView>(R.id.iv_project_list_atticle_ic)
            if (!item.picUrl.isNullOrEmpty()) {
                ImageLoaderManager.loadImage(mContext,"http://" + item.picUrl,imageView)
            }
        }
    }

}