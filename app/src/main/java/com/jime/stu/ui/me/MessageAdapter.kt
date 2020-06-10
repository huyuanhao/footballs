package com.jime.stu.ui.me

import android.text.TextPaint
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jime.stu.R
import com.jime.stu.bean.MesageBean
import com.jime.stu.utils.ImageLoaderManager
import com.scwang.smartrefresh.layout.util.DesignUtil

/**
 * @author PC
 * @date 2020/06/09 17:10
 */
class MessageAdapter : BaseQuickAdapter<MesageBean, BaseViewHolder>(R.layout.item_message) {
    override fun convert(helper: BaseViewHolder?, item: MesageBean?) {
//        ImageLoaderManager.loadCircleImage(mContext, item?.title, helper?.getView(R.id.image))
        helper?.setText(R.id.tv_title, item?.title)
        helper?.setText(R.id.tv_detail, item?.text)
        if (!TextUtils.isEmpty(item?.time) && !item?.time.equals("null")) {
            helper?.setText(R.id.tv_time,
                item?.time?.toLong()?.let { TimeUtils.millis2String(it) })
        }
        var tv_detail = helper?.getView<TextView>(R.id.tv_detail);
        var tv_more = helper?.getView<TextView>(R.id.tv_more);
        tv_more?.setOnClickListener {
            var more = tv_more.text.toString()
            if (more.equals("更多")) {
                tv_more.setText("收起")
                tv_detail?.maxLines = 10
            } else {
                tv_more.setText("更多")
                tv_detail?.maxLines = 1
                tv_detail?.ellipsize = TextUtils.TruncateAt.END
            }
        }
        //是否展示更多
        var vto2 = tv_detail?.getViewTreeObserver();
        vto2?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var mTextPaint = tv_detail?.getPaint();
                var mTextViewWidth = mTextPaint?.measureText(tv_detail?.text.toString())
                if (tv_detail != null) {
                    if (mTextViewWidth != null) {
                        if (mTextViewWidth > tv_detail.width) {//超出一行
                            tv_more?.visibility = View.VISIBLE
                        } else {
                            tv_more?.visibility = View.GONE
                        }
                    }
                }
            }
        });
    }

}