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

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.jime.stu.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


/** Fragment used for each individual page showing a photo inside of [GalleryFragment] */
class PhotoFragment internal constructor() : Fragment() {
    //长按后显示的 Item
    val items = arrayOf("保存图片")
    //图片转成Bitmap数组
    lateinit var bitmap:Bitmap;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ImageView(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments ?: return
        if (args.getString(FILE_NAME_KEY) == null) {
            val image = args.getString(IMG_URL)?.let { it } ?: R.drawable.ic_photo
            Glide.with(view).load(image).into(view as ImageView)
            Glide.with(view).asBitmap().load(image).into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                }

            })
        } else {
            val image = args.getString(FILE_NAME_KEY)?.let { File(it) } ?: R.drawable.ic_photo
            Glide.with(view).load(image).into(view as ImageView)
            bitmap = ImageUtils.getBitmap(image as File)
        }
        view.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                //弹出的“保存图片”的Dialog
                var builder = context?.let { AlertDialog.Builder(it) };
                builder?.setItems(items, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        when (which) {
                            0 ->
                                context?.let { saveImageToGallery(it, bitmap) };
                        }
                    }
                });
                builder?.show();
                return true
            }

        })
    }

    companion object {
        private const val FILE_NAME_KEY = "file_name"
        private const val IMG_URL = "img_url"

        fun create(image: File) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putString(FILE_NAME_KEY, image.absolutePath)
            }
        }

        fun create(image: String) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putString(IMG_URL, image)
            }
        }

        fun saveImageToGallery(context: Context, bmp: Bitmap) {
            // 首先保存图片 创建文件夹
            val appDir = File(Environment.getExternalStorageDirectory(), "识图");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            //图片文件名称
            val fileName = "识图_" + System.currentTimeMillis() + ".jpg";
            val file = File(appDir, fileName);
            try {
                val fos = FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (e: Exception) {
                Log.e("111", e.message);
                e.printStackTrace();
            }

//            // 其次把文件插入到系统图库
//            val path = file.getAbsolutePath();
//            try {
//                MediaStore.Images.Media.insertImage(
//                    context.getContentResolver(),
//                    path,
//                    fileName,
//                    null
//                );
//            } catch (e: FileNotFoundException) {
//                Log.e("333", e.message);
//                e.printStackTrace();
//            }
//            // 最后通知图库更新
//            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            val uri = Uri.fromFile(file);
//            intent.setData(uri);
//            context.sendBroadcast(intent);
            ToastUtils.showShort("保存成功");
        }
    }
}