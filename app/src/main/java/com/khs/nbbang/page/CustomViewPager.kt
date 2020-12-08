package com.khs.nbbang.page

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.khs.nbbang.freeUser.pageFragment.AddPeopleFragment
import com.khs.nbbang.freeUser.pageFragment.AddPlaceFragment
import com.khs.nbbang.freeUser.pageFragment.PeopleCountFragment
import com.khs.nbbang.freeUser.pageFragment.ResultPageFragment
import kotlinx.android.synthetic.main.fragment_select_count.*

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var mEnable: Boolean = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (mEnable) {
            return super.onTouchEvent(ev)
        } else return ev!!.action != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.v(this.javaClass.name,"onInterceptTouchEvent, childCount : ${childCount} , currentItem : $currentItem")
        var viewPageList = (adapter as CustomViewPagerAdapter).getViewPageList()
        if (checkPagingEnable(viewPageList.get(currentItem))) {
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

    fun checkPagingEnable(fragment : Fragment) : Boolean {
        var result = true
        if (fragment is AddPeopleFragment) {
            result = true
        } else if (fragment is AddPlaceFragment){
            result = true
        } else if (fragment is PeopleCountFragment) {
            var countNum = fragment.edit_count.text
            result.apply {
                if (TextUtils.isEmpty(countNum) || countNum.toString().equals("0")){
                    result = false
                }
            }
        } else if (fragment is ResultPageFragment) {
            result = true
        }
        setPagingEnable(result)
        return result
    }
}