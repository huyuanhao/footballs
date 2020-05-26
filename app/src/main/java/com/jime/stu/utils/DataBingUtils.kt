package com.jime.stu.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * @author PC
 * @date 2020/05/26 18:09
 */
object DataBingUtils {
    @BindingAdapter(value = ["android:setImageUrl"], requireAll = false)
    @JvmStatic
    fun setImageUrl(img: ImageView, url: String) {
        ImageLoaderManager.loadCircleImage(img.context,url,img)
    }
}