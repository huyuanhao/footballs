package com.jime.stu.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jime.stu.R

/**
 * @author PC
 * @date 2020/05/26 18:09
 */
object DataBingUtils {
    //    @BindingAdapter(value = ["url", "placeholder"], requireAll = false)
//    @JvmStatic
//    fun setImageUrl(img: ImageView, url: String) {
//        ImageLoaderManager.loadCircleImage(img.context,url,img)
//    }
    @BindingAdapter(value = ["circleUrl", "circlePlaceholder"], requireAll = false)
    @JvmStatic
    fun setImageUrl(imageView: ImageView, url: String, placeholder: Int) {
        Glide.with(imageView.context)
            .load(url)
            .priority(Priority.HIGH)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(placeholder)
            .into(imageView)

    }
}