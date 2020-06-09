package com.jime.stu.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayout
import com.jime.stu.R
import com.jime.stu.ui.me.MeFragment
import com.jime.stu.ui.photo.CameraFragment
import com.umeng.message.PushAgent
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private val titles = arrayOf("识图",  "我的")
    val tabIcons = arrayOf(R.drawable.tab_home,R.drawable.tab_me)
    private val fragments = ArrayList<Fragment>()
    var old = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_main)
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        //自定义函数检查权限是否拥有
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.CAMERA)
            .onGranted(Action<List<String?>> { permissions: List<String?>? ->
                initView()
            })
            .onDenied(Action<List<String?>> { permissions: List<String?>? ->
                ToastUtils.showShort("权限申请失败")
                finish()
            })
            .start()
    }

    private fun initView() {
        fragments.add(CameraFragment.newInstance())
//        fragments.add(ProjectFragment.newInstance())
        fragments.add(MeFragment.newInstance())
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragments[0])
            .commitNow()
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


//        nav_view.setOnNavigationItemSelectedListener(object :BottomNavigationView.OnNavigationItemSelectedListener{
//            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
//                when (p0.itemId) {
//                    R.id.homeFragment->{
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            MeFragmentDirections.actionMeToCamera())
//                        return true
//                    }
//                    R.id.meFragment->{
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            CameraFragmentDirections.actionCameraToMe())
//                        return true
//                    }
//                }
//                return false
//            }
//        })
        for (i in 0..1) { //为TabLayout添加10个tab并设置上文本
            tabLayout.addTab(tabLayout.newTab().setCustomView(getCustomView(i)))
        }
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
                if(p0?.position == 0){
                    toInput.toInput()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    0->{
                        if (old != 0) {
                            switchPage(0, old);
                            old = 0;
                        }
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            MeFragmentDirections.actionMeToCamera())
                    }
                    1->{
                        if (old != 1) {
                            switchPage(1, old);
                            old = 1;
                        }
//                        Navigation.findNavController(this@MainActivity, R.id.fragment_container).navigate(
//                            CameraFragmentDirections.actionCameraToMe())
                    }
                }
            }

        })
    }

    fun getCustomView(i:Int): View {
        val rootView: View = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null)
        var img = rootView.findViewById<AppCompatImageView>(R.id.img_title)
        var tv = rootView.findViewById<TextView>(R.id.txt_title)
        img.setImageDrawable(ContextCompat.getDrawable(this,tabIcons[i]));
        tv.setText(titles[i])
        return rootView
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
    lateinit var toInput :OnToInputClick ;
    fun setOnToInputClick(toInput:OnToInputClick){
        this.toInput = toInput
    }

    interface OnToInputClick{
        fun toInput();
    }
}
