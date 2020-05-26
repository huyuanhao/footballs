package com.jime.stu.ui.home

import android.content.Intent
import android.os.Bundle
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.event.Message
import com.jime.stu.R
import com.jime.stu.databinding.FragmentHomeBinding
import com.jime.stu.network.entity.NewsBean
import com.jime.stu.ui.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.projectViewModel = viewModel
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun lazyLoadData() {
        viewModel.getFirstData1()
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            0 -> {
                val bean = msg.obj as NewsBean
                val intent = Intent().apply {
                    setClass(requireActivity(), DetailActivity::class.java)
                    putExtra("url", bean.url)
                }
                startActivity(intent)
            }
        }
    }
}
