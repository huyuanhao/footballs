package com.jime.stu.widget

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.jime.stu.R
import com.jime.stu.WebActivity
import com.jime.stu.utils.Preference
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_agreement.view.*


/**
 * @author PC
 * @date 2020/06/10 17:25
 */
class InviterPopupView(context: Context) : CenterPopupView(context) {
    var userAgree by Preference(
        Preference.USERAGREE,
        "https://photoapi.jimetec.com/shitu/dist/agreement/userAgree.html"
    )
    var privateAgree by Preference(
        Preference.PRIVATEAGREE,
        "https://photoapi.jimetec.com/shitu/dist/agreement/privateAgree.html"
    )
    override fun getImplLayoutId(): Int {
        return  R.layout.dialog_agreement
    }

    override fun initPopupContent() {
        super.initPopupContent()
        val style = SpannableStringBuilder()
        //设置文字
        style.append(
            "请你务必审慎阅读、充分理解“服务及隐私政策”各条款，包括但不限于:为了向你提供位置等服务,我们需要收集你的设备信息、操作日志等个人信息。你可以在“设置”中查看、变更、删除个人信息并管理你的授权。\n" +
                    " 你可阅读《隐私协议》及《用户协议》了解详细信息。如你同意，请点击“同意\"开始接受我们的服务。"
        )
        //设置部分文字点击事件
        val clickableSpan: ClickableSpan =
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebActivity.startTo(
                        context,
                        privateAgree,
                        "隐私协议"
                    )
                }
            }
        //设置部分文字点击事件
        val clickableSpan2: ClickableSpan =
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    WebActivity.startTo(
                        context,
                        userAgree,
                        "用户协议"
                    )
                }
            }
        style.setSpan(clickableSpan, 103, 109, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        style.setSpan(clickableSpan2, 110, 116, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //        //配置给TextView
        tv_title.setMovementMethod(LinkMovementMethod.getInstance())
        tv_title.setText(style)
    }

}