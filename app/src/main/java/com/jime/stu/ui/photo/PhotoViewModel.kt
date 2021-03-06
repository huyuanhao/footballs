package com.jime.stu.ui.photo

import android.graphics.Bitmap
import android.view.View
import androidx.databinding.ObservableField
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.network.entity.NewsBean
import com.jime.stu.ui.MainActivity
import com.jime.stu.utils.FileUtils
import com.jime.stu.utils.InjectorUtil
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
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

    fun search(v: View) {

        var file = FileUtils.saveFile(photo.get(),photoString)
        Luban.with(v.context)
            .load(file) // 传人要压缩的图片列表
            .ignoreBy(100) // 忽略不压缩图片的大小
            .setTargetDir(MainActivity.getOutputDirectory(v.context).absolutePath) // 设置压缩后文件存储位置
            .setCompressListener(object : OnCompressListener {
                //设置回调
                override fun onStart() { // TODO 压缩开始前调用，可以在方法内启动 loading UI
                }

                override fun onSuccess(file: File) { // TODO 压缩成功后调用，返回压缩后的图片文件
                    LogUtils.e("压缩成功")
                    toSearch(file)
                }

                override fun onError(e: Throwable) { // TODO 当压缩过程出现问题时调用
                    LogUtils.e("压缩失败" + e.toString())
                    toSearch(file)
                }
            }).launch() //启动压缩
    }

    fun toSearch(file: File){
        launchOnlyresult({ homeRepository.uploadFile(file) }, {
            it.imgUrl = file.absolutePath
            defUI.msgEvent.postValue(Message(1,obj = it))
        },{
            defUI.msgEvent.postValue(Message(it.code,obj = it.errMsg))
        },{
           save("",  1, "识别按钮 ", "裁剪页面识别图片")
        },true,"识别中")

    }

    fun save(url:String,etypeInt:Int,title:String,mode:String){
        launchOnlyresult({homeRepository.save(url,etypeInt,title,mode)},{
            LogUtils.e("事件上报成功：url="+ url  +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{
            LogUtils.e("事件上报失败：url="+ url  +"etypeInt="+ etypeInt +"title="+ title +"mode="+ mode)
        },{},false)
    }

    interface OnItemClickListener {
        fun onItemClick(item: NewsBean)
    }

}