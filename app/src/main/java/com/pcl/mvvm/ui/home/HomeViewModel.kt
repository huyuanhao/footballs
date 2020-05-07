package com.pcl.mvvm.ui.home

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayout
import com.pcl.mvvm.BR
import com.pcl.mvvm.R
import com.pcl.mvvm.network.entity.NewsBean
import com.pcl.mvvm.network.entity.TypeBean
import com.pcl.mvvm.utils.InjectorUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import me.tatarka.bindingcollectionadapter2.ItemBinding


/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
class HomeViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    private val itemOnClickListener = object : OnItemClickListener {
        override fun onItemClick(item: NewsBean) {
            defUI.msgEvent.postValue(Message(0, obj = item))
        }
    }
    var title = ObservableField<String>()
    var navTitle = ObservableArrayList<String>()
    var navData = ObservableArrayList<TypeBean>()
//    var items = ObservableArrayList<ArticlesBean>()
//    var itemBinding = ItemBinding.of<ArticlesBean>(BR.itemBean, R.layout.item_project_list)
//        .bindExtra(BR.listenner, itemOnClickListener)

    var items = ObservableArrayList<NewsBean>()
    var itemBinding = ItemBinding.of<NewsBean>(BR.homeBean, R.layout.item_home_list)
        .bindExtra(BR.item_homeListenner, itemOnClickListener)

    private var page: Int = 1
    private var name: String = "全部"


    /**
     * 当一个请求结果，依赖另一个请求结果的时候，我们可以用 流的方式如下：
     *  以此类推，还可以 用  zip 操作符 对多个请求进行合并，以及 flatMapMerge、flatMapConcat 等。
     *  熟悉 RxJava 的你，分分钟钟可以上手的  （斜眼笑  `-` ）
     */
    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getFirstData() {
//        launchUI {
//            launchFlow { homeRepository.getNaviJson() }
//                .flatMapConcat {
//                    return@flatMapConcat if (it.isSuccess()) {
//                        navData.addAll(it.data)
//                        it.data.forEach { item -> navTitle.add(item.name) }
//                        launchFlow { homeRepository.getProjectList(page, it.data[0].id) }
//                    } else throw ResponseThrowable(it.errorCode, it.errorMsg)
//                }
//                .onStart { defUI.showDialog.postValue(null) }
//                .flowOn(Dispatchers.IO)
//                .onCompletion { defUI.dismissDialog.call() }
//                .catch {
//                    // 错误处理
//                    val err = ExceptionHandle.handleException(it)
//                    LogUtils.d("${err.code}: ${err.errMsg}")
//                }
//                .collect {
//                    if (it.isSuccess()) items.addAll(it.data.datas)
//                }
//        }

    }

    fun getFirstData1() {
        navData.add(TypeBean(name = "全部"))
        navData.add(TypeBean(name = "英超"))
        navData.add(TypeBean(name = "西甲"))
        navData.add(TypeBean(name = "德甲"))
        navData.add(TypeBean(name = "法甲"))
        navData.add(TypeBean(name = "意甲"))
        navData.add(TypeBean(name = "中超"))

        navData.forEach { item -> navTitle.add(item.name) }

        getNewsList(1, "全部", true)
    }

    fun getNewsList(page: Int, word: String, fresh: Boolean) {
        launchGo({
            if (fresh) {
                items.clear()
                items.addAll(homeRepository.getNewsList(page, word, fresh).newslist)
            } else {
                items.addAll(homeRepository.getNewsList(page, word, fresh).newslist)
            }
        }, {
            ToastUtils.showShort("暂无数据")
        })
    }

//    fun getProjectList(cid: Int) {
//        launchOnlyresult({ homeRepository.getProjectList(page, cid) }, {
//            items.clear()
//            items.addAll(it.datas)
//        })
//    }


    var tabOnClickListener = object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            p0?.let {
                //                getProjectList(navData[it.position].id)
                name = navData[it.position].name
                getNewsList(1, name, true)
            }
        }
    }

    var homeListenner = object : OnRefreshLoadMoreListener {
        override fun onLoadMore(refreshLayout: RefreshLayout) {
            page++
            getNewsList(page, name, false)
            refreshLayout.finishLoadMore()
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
            page = 1
            getNewsList(page, name, true)
            refreshLayout.finishRefresh()
        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: NewsBean)
    }
}