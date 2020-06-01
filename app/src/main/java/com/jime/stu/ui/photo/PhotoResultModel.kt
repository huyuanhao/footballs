package com.jime.stu.ui.photo

import android.graphics.Bitmap
import android.os.Environment
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.BR
import com.jime.stu.R
import com.jime.stu.bean.Info
import com.jime.stu.bean.SameInfo
import com.jime.stu.network.entity.NewsBean
import com.jime.stu.ui.project.ProjectViewModel
import com.jime.stu.utils.FileUtils
import com.jime.stu.utils.InjectorUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.io.File


/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
class PhotoResultModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    var photo = ObservableField<String>()
    var photoString = ""
    var title = ObservableField<String>()
    var baike = ObservableField<String>()

    private val itemXiangguanOnClickListener = object : OnItemClickListener {
        override fun onItemClick(item: Info) {
            defUI.msgEvent.postValue(Message(0, obj = item))
        }
    }
    private val itemSourceOnClickListener = object : OnItemSourceClickListener {
        override fun onItemClick(item: SameInfo) {
            defUI.msgEvent.postValue(Message(0, obj = item))
        }
    }
    var items = ObservableArrayList<Info>()
    var itemBinding = ItemBinding.of<Info>(BR.mInfo, R.layout.item_rv_xiangguan)
        .bindExtra(BR.item_meListenner, itemXiangguanOnClickListener)

    var itemsSource = ObservableArrayList<SameInfo>()
    var itemSourceBinding = ItemBinding.of<SameInfo>(BR.mSameInfo, R.layout.item_rv_source)
        .bindExtra(BR.item_sourceListenner, itemSourceOnClickListener)

    fun getNewsList(page: Int, word: String, fresh: Boolean) {
        launchGo({

        }, {
            ToastUtils.showShort("暂无数据")
        })
    }

    fun baikeDetail(){
        ToastUtils.showShort("baike")
    }

    fun xiangguanDetail(){
        ToastUtils.showShort("xiangguanDetail")
    }

    fun sourceDetail(){
        ToastUtils.showShort("sourceDetail")
    }

    fun back() {
        defUI.msgEvent.postValue(Message(0))
    }

    interface OnItemClickListener {
        fun onItemClick(item: Info)
    }

    interface OnItemSourceClickListener {
        fun onItemClick(item: SameInfo)
    }

}