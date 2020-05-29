package com.jime.stu.ui.photo

import android.graphics.Bitmap
import android.os.Environment
import androidx.databinding.ObservableField
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.network.entity.NewsBean
import com.jime.stu.utils.FileUtils
import com.jime.stu.utils.InjectorUtil
import java.io.File


/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
class PhotoViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    //    private val itemOnClickListener = object : OnItemClickListener {
//        override fun onItemClick(item: NewsBean) {
//            defUI.msgEvent.postValue(Message(0, obj = item))
//        }
//    }
    var photo = ObservableField<Bitmap>()
    var photoString = ""
    var title = ObservableField<String>()


    fun getNewsList(page: Int, word: String, fresh: Boolean) {
        launchGo({

        }, {
            ToastUtils.showShort("暂无数据")
        })
    }

    fun back() {
        defUI.msgEvent.postValue(Message(0))
    }

    fun left() {
        photo.set(ImageUtils.rotate(photo.get(), 270, 0f, 0f))
    }

    fun right() {
        photo.set(ImageUtils.rotate(photo.get(), 90, 0f, 0f))
    }

    fun search() {
        var file = FileUtils.saveFile(photo.get(),photoString)
        launchOnlyresult({ homeRepository.uploadFile(file) }, {
            ToastUtils.showShort("识别成功" + it.toString())
        })

    }

    interface OnItemClickListener {
        fun onItemClick(item: NewsBean)
    }

}