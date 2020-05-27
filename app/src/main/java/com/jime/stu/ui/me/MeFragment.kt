package com.jime.stu.ui.me

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.R
import com.jime.stu.bean.MeInfo
import com.jime.stu.databinding.MeFragmentBinding
import com.jime.stu.network.entity.UsedWeb
import com.jime.stu.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.me_fragment.*

class MeFragment : BaseFragment<MeViewModel, MeFragmentBinding>() {

    private val mAdapter by lazy { MeWebAdapter() }

    companion object {
        fun newInstance() = MeFragment()
    }

    override fun layoutId() = R.layout.me_fragment

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.meViewModel = viewModel
        mBinding?.fragment = this
    }


    override fun lazyLoadData() {
        viewModel.getItemList()
    }

    fun onFeelBackClick() {
        ToastUtils.showShort("反馈")
    }
    fun onSharedClick() {
        ToastUtils.showShort("分享")
    }
    fun onAgreementClick() {
        ToastUtils.showShort("协议")
    }
    fun onMessageClick() {
        ToastUtils.showShort("消息")
    }

    override fun handleEvent(msg: Message) {
        when(msg.code){
            1->{
                var info = msg.obj as MeInfo
                startActivity(Intent(activity,DetailActivity::class.java).putExtra("url",info.wordUrl))
            }
        }
    }
}
