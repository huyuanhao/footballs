package com.pcl.mvvm.ui.home

import android.content.Intent
import android.os.Bundle
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.event.Message
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.FragmentHomeBinding
import com.pcl.mvvm.network.entity.NewsBean
import com.pcl.mvvm.ui.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.homeViewModel = viewModel
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
