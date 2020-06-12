package com.jime.stu.ui.photo

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aleyn.mvvm.base.BaseActivity
import com.jime.stu.R
import com.jime.stu.bean.Product
import com.jime.stu.bean.Simipic
import com.jime.stu.databinding.ActivityProductBinding
import com.jime.stu.databinding.ActivitySimipicBinding
import kotlinx.android.synthetic.main.activity_simipic.*
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/02 09:44
 */
class SimipicActivity :BaseActivity<SimipicModel,ActivitySimipicBinding>(){
    lateinit var simipic:Simipic
    override fun layoutId(): Int {
        return R.layout.activity_simipic
    }
    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {
        simipic = intent.getSerializableExtra("simipic") as Simipic

        viewModel.items.addAll(simipic.listThumbUrl)

        tv_title.text = "相似图片"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerView.layoutManager = GridLayoutManager(this,4)
    }
}