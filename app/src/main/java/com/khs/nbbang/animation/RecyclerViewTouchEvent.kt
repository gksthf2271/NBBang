package com.khs.nbbang.animation

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewTouchEvent : RecyclerView.OnItemTouchListener {

    private var intercept = false

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return intercept
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    fun enable() {
        intercept = true
    }

    fun disable(){
        intercept = false
    }
}