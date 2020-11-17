package com.khs.nbbang.utils

import android.content.Context

class DisplayUtils() {

    fun getItemViewSize(context: Context): Int {
        return getItemViewSize(context, 1)
    }

    fun getItemViewSize(context: Context, count: Int): Int {
        val metrics = context.resources.displayMetrics
        val screenWidth = metrics.widthPixels
        return ((screenWidth * 0.95) / count).toInt()
    }
}