package com.pcl.mvvm.ui.guide

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pcl.mvvm.Bean.BannerInfo
import com.pcl.mvvm.R
import com.pcl.mvvm.ui.login.LoginActivity
import com.pcl.mvvm.utils.BannerImageLoader
import com.pcl.mvvm.utils.ImageLoaderManager
import com.pcl.mvvm.utils.InjectorUtil
import com.pcl.mvvm.utils.Preference
import com.stx.xhb.androidx.XBanner.XBannerAdapter
import kotlinx.android.synthetic.main.activity_guide.*


/**
 * @author PC
 * @date 2020/05/25 11:04
 */
class GuideActivity : Activity() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    var imgesUrl = mutableListOf<BannerInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        useBanner()
        loadData()
    }

    fun useBanner() { //--------------------------简单使用-------------------------------
        //添加轮播图片数据（图片数据不局限于网络图片、本地资源文件、View 都可以）,刷新数据也是调用该方法
        xbanner.loadImage(XBannerAdapter { banner, model, view, position ->
            val tvContent = view.findViewById(R.id.start) as TextView
            val img = view.findViewById(R.id.image) as ImageView
            img.setImageResource((model as BannerInfo).img)
            if(position == 3){
                tvContent.visibility = View.VISIBLE
            }else{
                tvContent.visibility = View.GONE
            }
            tvContent.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                var isFirst by Preference(Preference.IS_FIRST, true)
                isFirst = false
                finish()
            }
        })
    }

    fun loadData() {
        imgesUrl.add(BannerInfo(R.mipmap.guide1))
        imgesUrl.add(BannerInfo(R.mipmap.guide2))
        imgesUrl.add(BannerInfo(R.mipmap.guide3))
        imgesUrl.add(BannerInfo(R.mipmap.guide4))
        xbanner.setBannerData(
            R.layout.item_banner,
            imgesUrl
        );
    }
}