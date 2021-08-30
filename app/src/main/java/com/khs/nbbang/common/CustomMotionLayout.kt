package com.khs.nbbang.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {
    val TAG = this.javaClass.simpleName
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.let {
            Log.v(TAG,"action : ${ev!!.action}")
            when (ev.action) {
                MotionEvent.ACTION_MOVE -> {
                    it!!.requestDisallowInterceptTouchEvent(true)
                }
                else -> {

                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}