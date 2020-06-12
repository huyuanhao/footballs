package com.jime.stu.ui.photo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jime.stu.R
import kotlinx.android.synthetic.main.activity_picture_detail.*

import kotlinx.android.synthetic.main.toorbar.*


/**
 * @author PC
 * @date 2020/06/02 09:44
 */
class PictureDetailActivity : AppCompatActivity() {

//    lateinit var simipic:Simipic
    private lateinit var mediaList: MutableList<String>

    inner class MediaPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = mediaList.size
        override fun getItem(position: Int): androidx.fragment.app.Fragment =
            PhotoFragment.create(mediaList[position])

        override fun getItemPosition(obj: Any): Int = POSITION_NONE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_detail)
        initData()
    }

     fun initData() {
         mediaList = intent.getStringArrayListExtra("simipic")
         var position = intent.getIntExtra("position",0)
//         mediaList.clear()
//         mediaList.addAll(simipic.listThumbUrl)
         val mediaViewPager = viewPager.apply {
             offscreenPageLimit = 2
             adapter = MediaPagerAdapter(supportFragmentManager)
             addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                 override fun onPageScrollStateChanged(state: Int) {
                 }

                 override fun onPageScrolled(
                     position: Int,
                     positionOffset: Float,
                     positionOffsetPixels: Int
                 ) {
                 }

                 override fun onPageSelected(position: Int) {
                     tvTitle.text = (position+1).toString() + "/" + mediaList.size
                 }

             })
         }

         mediaViewPager.currentItem = position
         tvTitle.text = "1/" + mediaList.size
    }
}