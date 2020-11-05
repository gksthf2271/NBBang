package com.khs.nbbang.page.customView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var mEnable: Boolean = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (mEnable) {
            return super.onTouchEvent(ev)
        } else return ev!!.action != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (mEnable) {
            return super.onInterceptTouchEvent(ev)
        } else {
            if (ev!!.action == MotionEvent.ACTION_MOVE)
            else if (super.onInterceptTouchEvent(ev)) super.onTouchEvent(ev)
        }
        return false
    }


    fun setPagingEnable(isEnable: Boolean) {
        mEnable = isEnable
    }
}