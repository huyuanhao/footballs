package com.jime.stu.ui.photo

import android.os.Bundle
import com.aleyn.mvvm.base.BaseActivity
import com.jime.stu.R
import com.jime.stu.bean.Product
import com.jime.stu.databinding.ActivityProductBinding
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/02 09:44
 */
class ProductActivity :BaseActivity<ProductModel,ActivityProductBinding>(){
    lateinit var product:Product
    override fun layoutId(): Int {
        return R.layout.activity_product
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {
        product = intent.getSerializableExtra("product") as Product

        viewModel.items.addAll(product.listInfo)

        tv_title.text = "相关产品"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}