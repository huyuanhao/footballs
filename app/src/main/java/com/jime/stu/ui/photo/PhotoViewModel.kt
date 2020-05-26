package com.jime.stu.ui.photo

import androidx.databinding.ObservableField
import com.aleyn.mvvm.base.BaseViewModel
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.network.entity.NewsBean


/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
class PhotoViewModel : BaseViewModel() {

//    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
//    private val itemOnClickListener = object : OnItemClickListener {
//        override fun onItemClick(item: NewsBean) {
//            defUI.msgEvent.postValue(Message(0, obj = item))
//        }
//    }
    var title = ObservableField<String>()


    fun getNewsList(page: Int, word: String, fresh: Boolean) {
        launchGo({

        }, {
            ToastUtils.showShort("暂无数据")
        })
    }


    interface OnItemClickListener {
        fun onItemClick(item: NewsBean)
    }
}