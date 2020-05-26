package com.jime.stu.ui.photo

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.aleyn.mvvm.base.BaseFragment
import com.blankj.utilcode.util.ToastUtils
import com.jime.stu.R
import com.jime.stu.databinding.FragmentPhotoBinding
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.fragment_photo.*


/**
 * @author PC
 * @date 2020/05/22 14:12
 */
class PhotoFragments :BaseFragment<PhotoViewModel,FragmentPhotoBinding>()  {
    companion object {
        fun newInstance() = PhotoFragments()
    }

    override fun layoutId(): Int {
        return R.layout.fragment_photo
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView(savedInstanceState: Bundle?) {
            mBinding?.photoViewModel = viewModel

        //自定义函数检查权限是否拥有
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.CAMERA)
            .onGranted(Action<List<String?>> { permissions: List<String?>? ->
                camera.post { startCamera() }
            })
            .onDenied(Action<List<String?>> { permissions: List<String?>? ->
                ToastUtils.showShort("权限申请失败")
            })
            .start()

        camera.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCamera(){// 为取景器用例创建配置对象

    }
    private fun updateTransform() {

    }
}