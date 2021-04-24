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
import com.khs.nbbang.user.Member
import lv.chi.photopicker.loader.ImageLoader

class GlideUtils(){
    val TAG = this.javaClass.simpleName
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
            .circleCrop()
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
                    Log.v(TAG,"onResourceReady, resource : ${resource}")
                    return false
                }
            })
            .into(targetView)
    }

    fun drawMemberProfile(targetView: ImageView, member : Member, listener: RequestListener<Drawable>?) {
        when {
            !member.profileImage.isNullOrEmpty() -> {
                Log.v(TAG,"draw Member ProfileImage")
                drawImageWithString(targetView, member.profileImage, listener)
                return
            }
            !member.profileUri.isNullOrEmpty() -> {
                Log.v(TAG,"draw Member ProfileUrl")
                drawImageWithT(targetView, Uri.parse(member.profileUri), listener)
                return
            }
            !member.thumbnailImage.isNullOrEmpty() -> {
                Log.v(TAG,"draw Member thumbnailImage")
                drawImageWithT(targetView, Uri.parse(member.thumbnailImage), listener)
                return
            }
            else -> {
                Log.e(TAG,"Profile draw issue!, 프로필 이미지 확인 필요! profileImage : ${member.profileImage}, profileUrl : ${member.profileUri}.")
                drawImageWithT(targetView, null, listener)
                return
            }
        }
    }

    class GideImageLoader : ImageLoader {
        override fun loadImage(context: Context, view: ImageView, uri: Uri) {
            Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.icon_user)
                .error(R.drawable.ic_menu_gallery)
                .circleCrop()
                .into(view)
        }
    }
}