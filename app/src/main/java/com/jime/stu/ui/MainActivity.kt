package com.jime.stu.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.blankj.utilcode.util.BarUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jime.stu.R
import com.jime.stu.ui.me.MeFragmentDirections
import com.jime.stu.ui.photo.CameraFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var container: FrameLayout
    private val fragments = ArrayList<Fragment>()
    var old = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        container = findViewById(R.id.fragment_container)
        initView()
    }

    private fun initView() {
//        fragments.add(PhotoFragments.newInstance())
////        fragments.add(ProjectFragment.newInstance())
//        fragments.add(MeFragment.newInstance())
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.container, fragments[0])
//            .commitNow()
//
////        val navController = findNavController(R.id.nav_host_fragment)
////        nav_view.setupWithNavController(navController)
////        //取消默认的图片颜色切换效果,改用自定义的selector
////        nav_view.itemIconTintList = null
////
//        nav_view.setOnNavigationItemSelectedListener(object :
//            BottomNavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
//                when (p0.itemId) {
//                    R.id.homeFragment ->
//                        if (old != 0) {
//                            switchPage(0, old);
//                            old = 0;
//                            return true
//                        }
//                    R.id.meFragment ->
//                        if (old != 1) {
//                            switchPage(1, old);
//                            old = 1;
//                            return true
//                        }
////                    R.id.meFragment->
////                        if (old != 2) {
////                            switchPage(2, old);
////                            old = 2;
////                            return true
////                        }
//                }
////                switchPage(index, old)
//                return false
//            }
//        })

        nav_view.setOnNavigationItemSelectedListener(object :BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                when (p0.itemId) {
                    R.id.homeFragment->{
                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
                            MeFragmentDirections.actionMeToCamera())
                    }
                    R.id.meFragment->{
                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
                            CameraFragmentDirections.actionCameraToMe())
                    }
                }
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

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
//        container.postDelayed({
//            container.systemUiVisibility = FLAGS_FULLSCREEN
//        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    companion object {
        val KEY_EVENT_ACTION = "key_event_action"
        val KEY_EVENT_EXTRA = "key_event_extra"
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L
        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}
