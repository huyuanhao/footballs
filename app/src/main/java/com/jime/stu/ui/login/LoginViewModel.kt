package com.jime.stu.ui.login

import com.blankj.utilcode.util.RegexUtils

import android.os.CountDownTimer
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.network.entity.UsedWeb
import com.jime.stu.utils.InjectorUtil
import com.jime.stu.utils.Preference


/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
class LoginViewModel : BaseViewModel() {
    var userId by Preference(Preference.USER_ID, "")
    var userName by Preference(Preference.USER_NAME, "")
    var headImg by Preference(Preference.HEADIMAGE, "")
    var token by Preference(Preference.TOKEN, "")
    var is_login by Preference(Preference.IS_LOGIN, false)
    var phoneNumber by Preference(Preference.PHONE, "")

    val phone = ObservableField<String>("")
    var code = ObservableField("")
    var getCode = ObservableField("获取验证码")
    var code_enabled = ObservableField<Boolean>(true)
    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    var popularWeb = MutableLiveData<List<UsedWeb>>()

    fun getPopularWeb() {
        launchGo({
            val result = homeRepository.getPopularWeb()
            if (result.isSuccess()) {
                popularWeb.value = result.data
            }
        })
    }

    /**
     * 倒数计时器
     */
    private var timer: CountDownTimer = object : CountDownTimer(60 * 1000, 1000) {
        /**
         * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
         * @param millisUntilFinished
         */
        override fun onTick(millisUntilFinished: Long) {
            getCode.set((millisUntilFinished / 1000).toString())

            code_enabled.set(false)
        }

        /**
         * 倒计时完成时被调用
         */
        override fun onFinish() {
            getCode.set("重新获取验证码")
            code_enabled.set(true)
        }
    }

    fun GetVerificationCode() {
        if (TextUtils.isEmpty(phone.get().toString())) {
            ToastUtils.showShort("手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone.get().toString())) {
            ToastUtils.showShort("请输入正确的手机号")
            return
        }
        timer.start()
        //只返回结果，其他全抛自定义异常
        launchOnlyresult({ homeRepository.getCode(phone.get().toString()) }, {
            ToastUtils.showShort("发送成功")
        })
    }

    fun Login() {
//        getNewsList(1,"",true)
        if (TextUtils.isEmpty(phone.get().toString())) {
            ToastUtils.showShort("手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone.get().toString())) {
            ToastUtils.showShort("请输入正确的手机号")
            return
        }
        if (TextUtils.isEmpty(phone.get().toString())) {
            ToastUtils.showShort("验证码不能为空")
            return
        }
        if (11 > phone.get().toString()?.length ?: 0) {
            ToastUtils.showShort("请输入正确的验证码")
            return
        }
        launchOnlyresult({homeRepository.login(phone.get().toString(), code.get().toString())},{
            userId = it.userId.toString()
            userName = it.userName
            headImg = it.headImage
            token = it.token
            phoneNumber = phone.get().toString()
            defUI.msgEvent.postValue(Message(0))
            is_login = true
        })
//        launchGo({
//            var result = homeRepository.login(phone.get().toString(), code.get().toString())
//            if(result.code == 200) {
//                userId = result.data.userId.toString()
//                memberStatus = result.data.memberStatus
//                token = result.data.token
//                is_login = true
//            }else{
//                ToastUtils.showShort(result.message)
//            }
//        }, {
//            ToastUtils.showShort(it.errMsg)
//        })
    }

//    fun getNewsList(page: Int, word: String, fresh: Boolean) {
//        launchGo({
//            var s = homeRepository.getNewsList(page, word, fresh).newslist
//        }, {
//            ToastUtils.showShort("暂无数据")
//        })
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory() {
        timer.cancel()

    }
}