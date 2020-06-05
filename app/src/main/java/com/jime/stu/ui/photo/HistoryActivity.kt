package com.jime.stu.ui.photo

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.reflect.TypeToken
import com.jime.stu.R
import com.jime.stu.bean.History
import com.jime.stu.utils.ImageLoaderManager
import com.umeng.message.PushAgent
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.toorbar.*


/**
 * @author PC
 * @date 2020/06/02 09:57
 */
class HistoryActivity : AppCompatActivity() {
    lateinit var list: MutableList<History>;
    lateinit var adapter:HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_history)

        tv_title.text = "历史记录"
        tv_right.visibility = View.VISIBLE
        toolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        tv_right.setOnClickListener {
            if(tv_right.text.equals("编辑")){
                tv_right.text = "完成"
                adapter.setVisible(true)
                ll_bottom.visibility = View.VISIBLE
            }else{
                tv_right.text = "编辑"
                adapter.setVisible(false)
                ll_bottom.visibility = View.GONE
            }
            adapter.notifyDataSetChanged()
        }
        tv_select.setOnClickListener {
            if(tv_select.text.equals("全选")){
                tv_select.text = "全不选"
                for (history in list){
                    history.checked = true
                }
            }else{
                tv_select.text = "全选"
                for (history in list){
                    history.checked = false
                }
            }
            adapter.notifyDataSetChanged()
        }

        setDelete()
        //保存历史记录
        val js = CacheDiskStaticUtils.getString("history")
        val type = object : TypeToken<List<History?>?>() {}.type
        if(js!=null) {
            list = GsonUtils.fromJson<MutableList<History>>(js, type)
            adapter.setNewData(list)
        }
    }

    fun setDelete(){
        tv_delete.setOnClickListener {
            val mIterator = list.iterator()
            while (mIterator.hasNext()) {
                val next = mIterator.next()
                if (next.checked) {
                    mIterator.remove()
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CacheDiskStaticUtils.put("history", GsonUtils.toJson(list))
    }
}
class HistoryAdapter : BaseQuickAdapter<History,BaseViewHolder>(R.layout.item_history) {
    var visble = false
    override fun convert(helper: BaseViewHolder?, item: History?) {
        if (item != null) {
            if (helper != null) {
                ImageLoaderManager.loadImageWithCenterCrop(mContext,item.img,helper.getView(R.id.image))
                helper.setText(R.id.tv_title,item.title)
                helper.setText(R.id.tv_time,item.time)
                helper.setChecked(R.id.check,item.checked)

                helper.setGone(R.id.check,visble)
                helper.setOnCheckedChangeListener(R.id.check,object: CompoundButton.OnCheckedChangeListener{
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        mData.get(helper.adapterPosition).checked = isChecked
//                        notifyItemChanged(helper.adapterPosition)
                    }

                })
            }
        }
    }
    fun setVisible(visble:Boolean){
        this.visble = visble
    }
}