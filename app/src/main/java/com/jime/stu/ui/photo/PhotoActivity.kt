/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jime.stu.ui.photo

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.opengl.GLES10
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import androidx.core.view.drawToBitmap
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ImageUtils
import com.example.zhouwei.library.CustomPopWindow
import com.jime.stu.R
import com.jime.stu.bean.ImageDetail
import com.jime.stu.databinding.ActivityPhotoBinding
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.CountDownLatch


/** Fragment used for each individual page showing a photo inside of [GalleryFragment] */
class PhotoActivity : BaseActivity<PhotoViewModel, ActivityPhotoBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_photo
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.photoViewModel = viewModel

        var file = File(intent.getStringExtra("file"))
//        var rote = ImageUtils.getRotateDegree(file.absolutePath)
//            viewModel.photo.set(ImageUtils.rotate(ImageUtils.getBitmap(file),rote,0f,0f))
        viewModel.photoString = file.absolutePath

//        crop_image.setImageURI(ImageUtils.rotate(ImageUtils.getBitmap(file), rote, 0f, 0f))

        tv_search.setOnClickListener {
//            crop_image.drawToBitmap()
//            viewModel.photo.set(iv_crop.crop())
            viewModel.search(tv_search)
        }

    }

    override fun initData() {
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            0 -> {
                finish()
            }
            1 -> {
//                var detail = msg.obj as ImageDetail
//                var intent = Intent(this, PhotoResultActivity::class.java)
//                intent.putExtra("detail", detail)
//                startActivity(intent)
//                finish()
                CustomPopWindow.PopupWindowBuilder(this).setView(R.layout.dialog_open_member)
                    .create().showAtLocation(linearLayout, Gravity.CENTER, 0, 0)
            }
            103->{
                CustomPopWindow.PopupWindowBuilder(this).setView(R.layout.dialog_open_member)
                    .create().showAtLocation(linearLayout, Gravity.CENTER, 0, 0)
            }
        }
    }
}