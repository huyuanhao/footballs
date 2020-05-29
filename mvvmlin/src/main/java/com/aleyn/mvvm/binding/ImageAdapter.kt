package com.aleyn.mvvm.binding

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

/**
 *   @auther : Aleyn
 *   time   : 2019/11/07
 */
object ImageAdapter {

    @BindingAdapter(value = ["url", "placeholder"], requireAll = false)
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String, placeholder: Int) {
        LogUtils.e("ImageView==  " + url)
        var urls = url
        if(!url.startsWith("http")){
            urls = "http:" + url
        }
        Glide.with(imageView.context)
            .load(urls)
            .apply(RequestOptions().placeholder(placeholder))
            .into(imageView)

    }

    @BindingAdapter(value = ["imageFile"], requireAll = false)
    @JvmStatic
    fun setImageFile(imageView: ImageView, imageFile: File) {
        Glide.with(imageView.context)
            .load(imageFile)
            .apply(RequestOptions())
            .into(imageView)

    }

    @BindingAdapter(value = ["imageBitmap"], requireAll = false)
    @JvmStatic
    fun setImageBitmap(imageView: ImageView, bitmap: Bitmap) {
        Glide.with(imageView.context)
            .load(bitmap)
            .apply(RequestOptions())
            .into(imageView)

    }
}