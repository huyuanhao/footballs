package com.jime.stu.ui.me

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.R
import kotlinx.android.synthetic.main.activity_fell_back.*
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/09 11:18
 */
class FeelBackActivity :BaseActivity<FeelBackModel,ViewDataBinding>(){
    override fun layoutId(): Int {
        return R.layout.activity_fell_back
    }

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tv_title.text = "我的消息"
    }

    override fun initData() {
        tv_submit.setOnClickListener {
            if(edit.text.toString().length<=0){
                ToastUtils.showShort("请输入反馈内容！")
                return@setOnClickListener
            }
            val title = edit.text.toString()
            viewModel.upFeel(title)
        }
    }

    override fun handleEvent(msg: Message) {
        super.handleEvent(msg)
        if(msg.code == 1){
            finish()
        }
    }
}