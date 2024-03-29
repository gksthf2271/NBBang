package com.khs.nbbang.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

class DisplayUtils {

    fun getItemViewSize(context: Context): Int {
        return getItemViewSize(context, 1)
    }

    fun getItemViewSize(context: Context, count: Int): Int {
        val metrics = context.resources.displayMetrics
        val screenWidth = metrics.widthPixels
        return ((screenWidth * 0.95) / count).toInt()
    }

    fun getDisplaySize(context: Context): Point {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }
}