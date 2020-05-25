package com.pcl.mvvm.ui.login

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
import com.pcl.mvvm.network.entity.UsedWeb
import com.pcl.mvvm.utils.InjectorUtil


/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
class LoginViewModel : BaseViewModel() {

    var phone = ObservableField<String>("")
    var code = ObservableField("获取验证码")
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
            code.set((millisUntilFinished / 1000).toString())

            code_enabled.set(false)
            defUI.msgEvent.postValue(Message(0))
        }

        /**
         * 倒计时完成时被调用
         */
        override fun onFinish() {
            code.set("重新获取验证码")
            code_enabled.set(true)
        }
    }

    fun GetVerificationCode() {
        timer.start()
        //只返回结果，其他全抛自定义异常
        launchOnlyresult( { homeRepository.getCode(phone.get()!!) },{

        })
    }

    fun Login() {
        if(TextUtils.isEmpty(phone.get())){
            ToastUtils.showShort("手机号不能为空")
            return
        }
        if(!RegexUtils.isMobileExact(phone.get())){
            ToastUtils.showShort("请输入正确的手机号")
            return
        }
        if(TextUtils.isEmpty(phone.get())){
            ToastUtils.showShort("验证码不能为空")
            return
        }
        if(11> phone.get()?.length ?: 0){
            ToastUtils.showShort("请输入正确的验证码")
            return
        }

//        val result = homeRepository.getCode(phone.get()!!)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory() {
        timer.cancel()

    }
}