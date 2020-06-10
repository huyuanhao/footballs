package com.jime.stu.ui.me

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.jime.stu.R
import com.jime.stu.bean.History
import com.jime.stu.bean.MesageBean
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.toorbar.*
import org.greenrobot.eventbus.EventBus

/**
 * @author PC
 * @date 2020/06/09 16:57
 */
class MessageActivity :BaseActivity<MessageModel,ViewDataBinding>(){
    lateinit var mAdapter:MessageAdapter
    lateinit var list: MutableList<MesageBean>;
    override fun layoutId(): Int {
        return R.layout.activity_message
    }

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tv_title.text = "我的消息"
        EventBus.getDefault().post("nomessage")
    }

    override fun initData() {
        mAdapter = MessageAdapter()
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            adapter = mAdapter
        }

        val js = CacheDiskStaticUtils.getString("message")
        val type = object : TypeToken<List<MesageBean?>?>() {}.type
        if(js!=null) {
            list = GsonUtils.fromJson<MutableList<MesageBean>>(js, type)
            mAdapter.setNewData(list)
        }
    }
}