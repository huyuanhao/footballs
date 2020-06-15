package com.jime.stu.ui.photo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.GsonUtils
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.bean.ImageDetail
import com.jime.stu.databinding.ActivityPhotoResultBinding
import kotlinx.android.synthetic.main.activity_photo_result.*

/**
 * @author PC
 * @date 2020/06/01 10:13
 */
class PhotoResultActivity:BaseActivity<PhotoResultModel,ActivityPhotoResultBinding>(){
    lateinit var detail :ImageDetail;
    override fun layoutId(): Int {
        return R.layout.activity_photo_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewModel = viewModel
    }

    override fun initData() {
        detail = intent.getSerializableExtra("detail") as ImageDetail

        viewModel.photo.set(detail.cardHeader.imageUrl)

        viewModel.title.set(detail.cardHeader.maybeName)

        viewModel.baike.set(detail.baike.desc)

        viewModel.items.addAll(detail.product.listInfo)

        viewModel.itemsSource.addAll(detail.same.listSameInfo)

        viewModel.itemRelated.addAll(detail.simipic.listThumbUrl)

        if(TextUtils.isEmpty(detail.cardHeader.maybeName) && TextUtils.isEmpty(detail.baike.desc) &&
            detail.product.listInfo.size == 0 && detail.same.listSameInfo.size==0&& detail.simipic.listThumbUrl.size==0){
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
        tv_error.setOnClickListener {
            viewModel.upError(detail.imgUrl,GsonUtils.toJson(detail))
        }
    }

    override fun handleEvent(msg: Message) {
       when(msg.code){
           0->{
               finish()
           }
           1->{
//               val intent = Intent(this, BaikeActivity::class.java)
//               intent.putExtra("baike", detail.baike.moreUrl)
//               startActivity(intent)
               startActivity(
                   Intent(this, WebActivity::class.java)
                       .putExtra("url", detail.baike.moreUrl).putExtra("title", "百科")
               )
           }
           2->{
               val intent = Intent(this, ProductActivity::class.java)
               intent.putExtra("product", detail.product)
               startActivity(intent)
           }
           3->{
               val intent = Intent(this, ImageResourceActivity::class.java)
               intent.putExtra("same", detail.same)
               startActivity(intent)
           }
           4->{
               val intent = Intent(this, SimipicActivity::class.java)
               intent.putExtra("simipic", detail.simipic)
               startActivity(intent)
           }
       }
    }
}