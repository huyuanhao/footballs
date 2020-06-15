package com.jime.stu.ui.photo

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.reflect.TypeToken
import com.jime.stu.R
import com.jime.stu.bean.History
import com.jime.stu.bean.ImageDetail
import com.jime.stu.utils.ImageLoaderManager
import com.lxj.xpopup.XPopup
import com.umeng.message.PushAgent
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.toorbar.*
import java.io.File
import java.util.*


/**
 * @author PC
 * @date 2020/06/02 09:57
 */
class HistoryActivity : BaseActivity<HistoryViewModel,ViewDataBinding>() {
    lateinit var list: MutableList<History>;
    lateinit var mAdapter: HistoryAdapter

    override fun layoutId(): Int = R.layout.activity_history

    override fun initView(savedInstanceState: Bundle?) {
        PushAgent.getInstance(this).onAppStart();
        tv_title.text = "历史记录"
        tv_right.visibility = View.VISIBLE
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun initData() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = HistoryAdapter()
        var footView = layoutInflater.inflate(R.layout.item_bottom_his, null)
        mAdapter.addFooterView(footView)
        recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                XPopup.Builder(this@HistoryActivity).asConfirm(
                    "提示", "是否要识别这张图片", "取消", "识别", {
                        if (adapter != null) {
                            viewModel.toSearch(File(mAdapter.data[position].img))
                        }
                    }, { },false
                ).show()
            }
        })

        tv_right.setOnClickListener {
            if (tv_right.text.equals("编辑")) {
                tv_right.text = "完成"
                mAdapter.setVisible(true)
                ll_bottom.visibility = View.VISIBLE
            } else {
                tv_right.text = "编辑"
                mAdapter.setVisible(false)
                ll_bottom.visibility = View.GONE
            }
            mAdapter.notifyDataSetChanged()
        }
        tv_select.setOnClickListener {
            if (tv_select.text.equals("全选")) {
                tv_select.text = "全不选"
                for (history in list) {
                    history.checked = true
                }
            } else {
                tv_select.text = "全选"
                for (history in list) {
                    history.checked = false
                }
            }
            mAdapter.notifyDataSetChanged()
        }

        setDelete()
        //保存历史记录
        val js = CacheDiskStaticUtils.getString("history")
        val type = object : TypeToken<List<History?>?>() {}.type
        if (js != null) {
            list = GsonUtils.fromJson<MutableList<History>>(js, type)
            if (list.size == 0) {
                tv_right.visibility = View.GONE
                ll_bottom.visibility = View.GONE
                ll_null.visibility = View.VISIBLE
            }
            mAdapter.setNewData(list)
        }
    }

    fun setDelete() {
        tv_delete.setOnClickListener {
            val mIterator = list.iterator()
            while (mIterator.hasNext()) {
                val next = mIterator.next()
                if (next.checked) {
                    mIterator.remove()
                }
            }
            mAdapter.notifyDataSetChanged()
            if (list.size == 0) {
                tv_right.visibility = View.GONE
                ll_bottom.visibility = View.GONE
                ll_null.visibility = View.VISIBLE
            }
        }
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            1 -> {
                val detail = msg.obj as ImageDetail?
                val intent =
                    Intent(this, PhotoResultActivity::class.java)
                intent.putExtra("detail", detail)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        CacheDiskStaticUtils.put("history", GsonUtils.toJson(list))
    }

}

class HistoryAdapter : BaseQuickAdapter<History, BaseViewHolder>(R.layout.item_history) {
    var visble = false
    override fun convert(helper: BaseViewHolder?, item: History?) {
        if (item != null) {
            if (helper != null) {
                ImageLoaderManager.loadImageWithCenterCrop(
                    mContext,
                    item.img,
                    helper.getView(R.id.image)
                )
                helper.setText(R.id.tv_title, item.title)
                helper.setText(R.id.tv_time, item.time)
                helper.setChecked(R.id.check, item.checked)

                helper.setGone(R.id.check, visble)
                helper.setOnCheckedChangeListener(R.id.check,
                    object : CompoundButton.OnCheckedChangeListener {
                        override fun onCheckedChanged(
                            buttonView: CompoundButton?,
                            isChecked: Boolean
                        ) {
                            mData.get(helper.adapterPosition).checked = isChecked
//                        notifyItemChanged(helper.adapterPosition)
                        }

                    })
            }
        }
    }

    fun setVisible(visble: Boolean) {
        this.visble = visble
    }
}