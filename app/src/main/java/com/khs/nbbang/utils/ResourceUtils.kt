package com.khs.nbbang.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.khs.nbbang.R

class ResourceUtils {
    fun getDrawable(context: Context, resId: Int): Drawable? {
        return ResourcesCompat.getDrawable(context.resources, resId, null)
    }
}