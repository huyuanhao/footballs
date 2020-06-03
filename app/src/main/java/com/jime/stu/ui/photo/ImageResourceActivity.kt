package com.jime.stu.ui.photo

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.aleyn.mvvm.base.BaseActivity
import com.jime.stu.R
import com.jime.stu.bean.Product
import com.jime.stu.bean.Same
import com.jime.stu.databinding.ActivityImageResourceBinding
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/02 09:14
 */
class ImageResourceActivity :BaseActivity<ImageResourceModel,ActivityImageResourceBinding>(){
    lateinit var same:Same
    override fun layoutId(): Int {
        return R.layout.activity_image_resource
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {
        same = intent.getSerializableExtra("same") as Same

        viewModel.itemsSource.addAll(same.listSameInfo)

        tv_title.text = "图片来源"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}