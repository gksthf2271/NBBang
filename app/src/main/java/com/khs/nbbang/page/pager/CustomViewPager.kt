package com.khs.nbbang.page.pager

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.khs.nbbang.page.dutchPayPageFragments.AddPeopleFragment
import com.khs.nbbang.page.dutchPayPageFragments.AddPlaceFragment
import com.khs.nbbang.page.dutchPayPageFragments.PeopleCountFragment
import com.khs.nbbang.page.dutchPayPageFragments.ResultPageFragment
import kotlinx.android.synthetic.main.fragment_people_count.*

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    val DEBUG = true
    val TAG = this.javaClass.name
    private var mEnable: Boolean = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (mEnable) {
            return super.onTouchEvent(ev)
        } else return ev!!.action != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.v(TAG,"onInterceptTouchEvent, childCount : ${childCount} , currentItem : $currentItem")
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
        if (!fragment.isAdded) return result
        if (fragment is AddPeopleFragment) {
            if (DEBUG) Log.v(TAG,"currentFragment : ${AddPeopleFragment::class.java.name}")
            result = true
        } else if (fragment is AddPlaceFragment){
            if (DEBUG) Log.v(TAG,"currentFragment : ${AddPlaceFragment::class.java.name}")
            result = true
        } else if (fragment is PeopleCountFragment) {
            if (DEBUG) Log.v(TAG,"currentFragment : ${PeopleCountFragment::class.java.name}")
            var countNum = fragment.txt_count.text
            result.apply {
                if (TextUtils.isEmpty(countNum) || countNum.toString().equals("0")) {
                    result = false
                }
            }
        } else if (fragment is ResultPageFragment) {
            if (DEBUG) Log.v(TAG,"currentFragment : ${ResultPageFragment::class.java.name}")
            result = true
        }
        setPagingEnable(result)
        return result
    }
}