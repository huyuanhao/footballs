package com.jime.stu.ui.photo

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseActivity
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.bean.Product
import com.jime.stu.databinding.ActivityProductBinding
import com.jime.stu.ui.me.MeWebAdapter
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/02 09:44
 */
class ProductActivity : BaseActivity<ProductModel, ActivityProductBinding>() {
    lateinit var product: Product
    private val mAdapter by lazy { ProductAdapter() }
    override fun layoutId(): Int {
        return R.layout.activity_product
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {
        product = intent.getSerializableExtra("product") as Product

        tv_title.text = "相关产品"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        var footView = layoutInflater.inflate(R.layout.item_bottom, null)
        mAdapter.addFooterView(footView)
        mAdapter.setNewData(product.listInfo)
        mAdapter.setOnItemClickListener { _, _, position ->
            val url = mAdapter.getItem(position)?.buyurl
            if (url != null) {
                viewModel.save(url, 1, "跳转的HTML页面", "跳转的HTML页面")
            }
            startActivity(
                Intent(this@ProductActivity, WebActivity::class.java)
                    .putExtra("url", url).putExtra("title", "相关产品")
            )
        }
    }
}