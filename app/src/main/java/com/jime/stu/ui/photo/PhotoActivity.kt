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

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import androidx.core.graphics.drawable.toIcon
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.databinding.ViewDataBinding
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jime.stu.R
import com.jime.stu.bean.ImageDetail
import com.jime.stu.databinding.ActivityPhotoBinding
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.File
import java.io.Serializable


/** Fragment used for each individual page showing a photo inside of [GalleryFragment] */
class PhotoActivity :BaseActivity<PhotoViewModel,ActivityPhotoBinding>() {

    override fun layoutId(): Int {
        return R.layout.activity_photo
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.photoViewModel = viewModel

        var uri = intent.getStringExtra("uri").toUri()
        if(!TextUtils.isEmpty(uri.toString())){
            var rote = ImageUtils.getRotateDegree(uri.toFile().absolutePath)
            viewModel.photo.set(ImageUtils.rotate(ImageUtils.getBitmap(uri.toFile()),rote,0f,0f))
            viewModel.photoString = uri.toFile().absolutePath
        }
    }

    override fun initData() {
    }

    override fun handleEvent(msg: Message) {
        when(msg.code){
            0->{
                finish()
            }
            1->{
                var detail = msg.obj as ImageDetail
                var intent = Intent(this, PhotoResultActivity::class.java)
                intent.putExtra("detail", detail)
                startActivity(intent)
                finish()
            }
        }
    }
}