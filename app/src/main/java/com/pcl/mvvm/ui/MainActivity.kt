package com.pcl.mvvm.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pcl.mvvm.R
import com.pcl.mvvm.ui.me.MeFragment
import com.pcl.mvvm.ui.home.HomeFragment
import com.pcl.mvvm.ui.project.ProjectFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragments = ArrayList<Fragment>()
    var old = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        initView()
    }

    private fun initView() {
        fragments.add(HomeFragment.newInstance())
        fragments.add(ProjectFragment.newInstance())
        fragments.add(MeFragment.newInstance())
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragments[0])
            .commitNow()

//        val navController = findNavController(R.id.nav_host_fragment)
//        nav_view.setupWithNavController(navController)
//        //取消默认的图片颜色切换效果,改用自定义的selector
//        nav_view.itemIconTintList = null
//
        nav_view.setOnNavigationItemSelectedListener(object :BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                when(p0.itemId){
                    R.id.homeFragment->
                        if (old != 0) {
                            switchPage(0, old);
                            old = 0;
                            return true
                    }
                    R.id.projectFragment->
                        if (old != 1) {
                            switchPage(1, old);
                            old = 1;
                            return true
                        }
                    R.id.meFragment->
                        if (old != 2) {
                            switchPage(2, old);
                            old = 2;
                            return true
                        }
                }
//                switchPage(index, old)
                return false
            }
        })
    }

    private fun switchPage(index: Int, old: Int) {
        val now = fragments[index]
        supportFragmentManager.beginTransaction().apply {
            if (!now.isAdded) {
                add(R.id.container, now)
            }
            show(now)
            hide(fragments[old])
            commit()
        }
    }
}
