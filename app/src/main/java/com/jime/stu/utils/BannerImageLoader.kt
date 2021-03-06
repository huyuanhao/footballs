package com.jime.stu.utils

import android.view.View
import android.widget.ImageView
import com.jime.stu.network.entity.BannerBean
import com.stx.xhb.androidx.XBanner

/**
 *   @auther : Aleyn
 *   time   : 2019/09/05
 */
class BannerImageLoader : XBanner.XBannerAdapter {

    override fun loadBanner(banner: XBanner?, model: Any?, view: View?, position: Int) {
//        Glide.with(banner!!.context).load((model as BannerBean).xBannerUrl).into(view as ImageView)

        (view as ImageView).setImageResource((model as BannerBean).img)
    }

}