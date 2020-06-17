package com.jime.stu.ui.photo

import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.BR
import com.jime.stu.R
import com.jime.stu.WebActivity
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
        override fun onItemClick(view: View,item: Info) {
            save(item.buyurl,1,"跳转的HTML页面","跳转的HTML页面")
            view.context.startActivity(
                Intent(view.context, WebActivity::class.java)
                    .putExtra("url", item.buyurl).putExtra("title", "相关产品")
            )
        }
    }
    private val itemSourceOnClickListener = object : OnItemSourceClickListener {
        override fun onItemClick(view: View,item: SameInfo) {
            save(item.url,1,"跳转的HTML页面","跳转的HTML页面")
            view.context.startActivity(
                Intent(view.context, WebActivity::class.java)
                    .putExtra("url", item.url).putExtra("title", "图片来源")
            )
        }
    }

    private val itemRelatedOnClickListener = object : OnItemRelatedClickListener {
        override fun onItemClick(view: View,item: String) {
            view.context.startActivity(
                Intent(view.context, PictureDetailActivity::class.java)
                    .putExtra("simipic", itemRelated)
                    .putExtra("position", itemRelated.indexOf(item))
            )
        }
    }


    var items = ObservableArrayList<Info>()
    var itemBinding = ItemBinding.of<Info>(BR.mInfo, R.layout.item_rv_xiangguan)
        .bindExtra(BR.item_meListenner, itemXiangguanOnClickListener)

    var itemsSource = ObservableArrayList<SameInfo>()
    var itemSourceBinding = ItemBinding.of<SameInfo>(BR.mSameInfo, R.layout.item_rv_source)
        .bindExtra(BR.item_sourceListenner, itemSourceOnClickListener)

    var itemRelated = ObservableArrayList<String>()
    var itemRelatedBinding = ItemBinding.of<String>(BR.mSimipic, R.layout.item_rv_related)
        .bindExtra(BR.item_relatedListenner, itemRelatedOnClickListener)

    fun getNewsList(page: Int, word: String, fresh: Boolean) {
        launchGo({

        }, {
            ToastUtils.showShort("暂无数据")
        })
    }

    fun baikeDetail(){
        defUI.msgEvent.postValue(Message(1))
    }

    fun xiangguanDetail(){
        defUI.msgEvent.postValue(Message(2))
    }

    fun sourceDetail(){
        defUI.msgEvent.postValue(Message(3))
    }

    fun relatedDetail(){
        defUI.msgEvent.postValue(Message(4))
    }

    fun back() {
        defUI.msgEvent.postValue(Message(0))
    }

    fun upError(imgUrl: String,data:String) {
        launchOnlyresult({
            homeRepository.upError(imgUrl,data)
        }, {
            ToastUtils.showShort("感谢您的反馈")
        })
    }

    fun save(url:String,etypeInt:Int,title:String,mode:String){
        launchOnlyresult({homeRepository.save(url,etypeInt,title,mode)},{
            LogUtils.e("事件上报成功：url="+ url  +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{
            LogUtils.e("事件上报失败：url="+ url  +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{},false)
    }

    interface OnItemClickListener {
        fun onItemClick(view : View, item: Info)
    }

    interface OnItemSourceClickListener {
        fun onItemClick(view: View,item: SameInfo)
    }

    interface OnItemRelatedClickListener {
        fun onItemClick(view: View,item: String)
    }
}