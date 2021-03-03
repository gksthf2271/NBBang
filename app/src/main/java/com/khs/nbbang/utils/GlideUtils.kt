package com.khs.nbbang.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khs.nbbang.R
import lv.chi.photopicker.loader.ImageLoader

class GlideUtils(){
    val TAG = this.javaClass.name
    fun drawImageWithString(targetView : ImageView?, strRes: String?, listener : RequestListener<Drawable>?) {
        targetView ?: return
        drawImageWithT(targetView, strRes, listener)
    }

    fun <T> drawImageWithT(targetView : ImageView?, res: T?, listener : RequestListener<Drawable>?) {
        targetView ?: return
        Log.v(TAG,"drawImageWithT res : $res")

        Glide.with(targetView.context)
            .load(res ?: R.drawable.icon_user)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .fitCenter()
            .skipMemoryCache(false)
            .listener(listener ?: object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.v(TAG,"onLoadFailed, exception : $e")
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.v(TAG,"onResourceReady, resource : $resource")
                    return true
                }
            })
            .into(targetView)
    }

    class GideImageLoader : ImageLoader {
        override fun loadImage(context: Context, view: ImageView, uri: Uri) {
            Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.icon_user)
                .error(R.drawable.ic_menu_gallery)
                .fitCenter()
                .into(view)
        }
    }
}