package com.jime.stu.ui.photo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jime.stu.R
import com.jime.stu.bean.Baike
import com.jime.stu.bean.Product
import com.umeng.message.PushAgent
import kotlinx.android.synthetic.main.activity_baike.*
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/02 09:57
 */
class BaikeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_baike)

        var baike = intent.getSerializableExtra("baike") as Baike
        tv_detail.setText(baike.desc)

        tv_title.text = "百科"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}