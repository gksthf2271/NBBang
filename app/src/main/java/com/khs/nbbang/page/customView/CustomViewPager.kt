package com.khs.nbbang.page.customView

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.cview_select_count.view.*

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var mEnable: Boolean = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (mEnable) {
            return super.onTouchEvent(ev)
        } else return ev!!.action != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (checkPagingEnable(get(currentItem))) {
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

    fun checkPagingEnable(view : View) : Boolean {
        var result = true
        if (view is AddPeopleView) {
            result = true
        } else if (view is AddPlaceView){
            result = true
        } else if (view is PeopleCountView) {
            var countNum = view.edit_count.text
            result.apply {
                if (TextUtils.isEmpty(countNum) || countNum.toString().equals("0")){
                    result = false
                }
            }
        } else if (view is ResultPageView) {
            result = true
        }
        setPagingEnable(result)
        return result
    }
}