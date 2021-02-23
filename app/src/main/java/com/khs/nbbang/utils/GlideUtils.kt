package com.khs.nbbang.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.khs.nbbang.R

class GlideUtils() {
    fun drawImage(targetView : ImageView?, res: String?, listener : RequestListener<Drawable>?) {
        targetView ?: return
        Glide.with(targetView.context)
            .load(res ?: R.drawable.icon_user)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .circleCrop()
            .skipMemoryCache(false)
            .listener(listener)
            .into(targetView)
    }

    fun drawImageWithResId(targetView : ImageView?, res: Int?, listener : RequestListener<Drawable>?) {
        targetView ?: return
        Glide.with(targetView.context)
            .load(res ?: R.drawable.icon_user)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .circleCrop()
            .skipMemoryCache(false)
            .listener(listener)
            .into(targetView)
    }
}