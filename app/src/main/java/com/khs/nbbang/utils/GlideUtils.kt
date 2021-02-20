package com.khs.nbbang.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.khs.nbbang.R

class GlideUtils() {
    fun drawImageWith(context: Context, targetView : ImageView?, res: String?, listener : RequestListener<Drawable>?) {
        targetView ?: return
        Glide.with(context)
            .load(res ?: R.drawable.icon_user)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .circleCrop()
            .skipMemoryCache(false)
            .listener(listener)
            .into(targetView)
    }
}