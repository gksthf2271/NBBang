package com.khs.nbbang.utils

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnItemTouchListener(
    onTouch : ((motionEvent : MotionEvent) -> Unit)
) {
    addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            onTouch.invoke(e)
            return false
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        }

    })
}

fun RecyclerView.setOnScorllingListenenr(
    isUpAction : ((isUpAction : Boolean) -> Unit)
){
    addOnScrollListener(object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                isUpAction(true)
            } else {
                isUpAction(false)
            }
        }
    })
}
