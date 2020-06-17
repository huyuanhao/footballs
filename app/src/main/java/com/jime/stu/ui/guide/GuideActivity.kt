package com.jime.stu.ui.guide

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jime.stu.bean.BannerInfo
import com.jime.stu.R
import com.jime.stu.ui.MainActivity
import com.jime.stu.utils.InjectorUtil
import com.jime.stu.utils.Preference
import com.stx.xhb.androidx.XBanner.XBannerAdapter
import com.umeng.message.PushAgent
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
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_guide)

        useBanner()
        loadData()
    }

    fun useBanner() { //--------------------------简单使用-------------------------------
        //添加轮播图片数据（图片数据不局限于网络图片、本地资源文件、View 都可以）,刷新数据也是调用该方法
        xbanner.loadImage(XBannerAdapter { banner, model, view, position ->
            val tvContent = view.findViewById(R.id.start) as TextView
            val tvOpen = view.findViewById(R.id.tv_open) as TextView
            val img = view.findViewById(R.id.image) as ImageView
            img.setImageResource((model as BannerInfo).img)
            if(position == 1){
                tvOpen.visibility = View.VISIBLE
            }else{
                tvOpen.visibility = View.GONE
            }
            if(position == 3){
                tvContent.visibility = View.VISIBLE
            }else{
                tvContent.visibility = View.GONE
            }
            tvContent.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
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