package com.jime.stu.ui.photo

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.bean.Product
import com.jime.stu.bean.Same
import com.jime.stu.databinding.ActivityImageResourceBinding
import kotlinx.android.synthetic.main.activity_simipic.*
import kotlinx.android.synthetic.main.toorbar.*

/**
 * @author PC
 * @date 2020/06/02 09:14
 */
class ImageResourceActivity :BaseActivity<ImageResourceModel,ActivityImageResourceBinding>(){
    lateinit var same:Same
    private val mAdapter by lazy { ImageResourceAdapter() }
    override fun layoutId(): Int {
        return R.layout.activity_image_resource
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {
        same = intent.getSerializableExtra("same") as Same

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@ImageResourceActivity)
            adapter = mAdapter
        }
        var footView = layoutInflater.inflate(R.layout.item_bottom, null)
        mAdapter.addFooterView(footView)
        mAdapter.setNewData(same.listSameInfo)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val url = mAdapter.getItem(position)?.url
            if (url != null) {
                viewModel.save(url, 1, "跳转的HTML页面", "跳转的HTML页面")
            }
            view.context.startActivity(
                Intent(view.context, WebActivity::class.java)
                    .putExtra("url", url).putExtra("title", "图片来源")
            )
        }

        tv_title.text = "图片来源"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}