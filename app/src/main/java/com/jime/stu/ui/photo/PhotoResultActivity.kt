package com.jime.stu.ui.photo

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.aleyn.mvvm.base.BaseActivity
import com.jime.stu.R
import com.jime.stu.bean.ImageDetail
import com.jime.stu.databinding.ActivityPhotoResultBinding
import kotlinx.android.synthetic.main.activity_photo_result.*

/**
 * @author PC
 * @date 2020/06/01 10:13
 */
class PhotoResultActivity:BaseActivity<PhotoResultModel,ActivityPhotoResultBinding>(){
    override fun layoutId(): Int {
        return R.layout.activity_photo_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {
        val detail = intent.getSerializableExtra("detail") as ImageDetail

        viewModel.photo.set(detail.cardHeader.imageUrl)

        viewModel.title.set(detail.cardHeader.maybeName)

        viewModel.baike.set(detail.baike.desc)

        viewModel.items.addAll(detail.product.listInfo)

        viewModel.itemsSource.addAll(detail.same.listSameInfo)

        if(TextUtils.isEmpty(detail.cardHeader.maybeName) && TextUtils.isEmpty(detail.baike.desc) &&
            detail.product.listInfo.size == 0 && detail.same.listSameInfo.size==0){
            ll_null.visibility = View.VISIBLE
        }else{
            ll_null.visibility = View.GONE
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tv_huan.setOnClickListener{
            finish()
        }
    }
}