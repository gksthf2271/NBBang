package com.khs.nbbang.common

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import com.khs.nbbang.utils.LogUtil

class CustomMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {
    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_UI
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent?.let {
            ev?.let { ev ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "action : ${ev.action}")
                when (ev.action) {
                    MotionEvent.ACTION_MOVE -> {
                        it.requestDisallowInterceptTouchEvent(true)
                    }
                    else -> {

                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}