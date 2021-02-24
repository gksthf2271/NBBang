package com.khs.nbbang.utils

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnItemTouchListener(
    onTouch : ((motionEvent : MotionEvent) -> Unit)
) {
    addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            onTouch?.invoke(e)
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            onTouch?.invoke(e)
            return false
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        }

    })
}
