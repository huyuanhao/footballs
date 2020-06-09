package com.jime.stu.ui.me

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseActivity
import com.jime.stu.R
import com.jime.stu.bean.MesageBean
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/09 16:57
 */
class MessageActivity :BaseActivity<MessageModel,ViewDataBinding>(){
    lateinit var mAdapter:MessageAdapter
    override fun layoutId(): Int {
        return R.layout.activity_message
    }

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tv_title.text = "我的消息"
    }

    override fun initData() {
        mAdapter = MessageAdapter()
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            adapter = mAdapter
        }

        for(i in 0..10){
            mAdapter.addData(MesageBean("open","标题","文本","时间"))
        }
    }
}