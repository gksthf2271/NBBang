package com.khs.nbbang.page

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.khs.nbbang.base.BaseFragment

class CustomViewPagerAdapter(fm: FragmentManager, fragments: MutableList<BaseFragment>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val TAG = this.javaClass.name
    var mFragmentList: MutableList<BaseFragment>
    var mFragmentManager : FragmentManager

    init {
        mFragmentList = fragments
        mFragmentManager = fm
    }

    fun getViewPageList() : MutableList<BaseFragment> {
        return mFragmentList
    }

    override fun getItem(position: Int): BaseFragment {
        Log.v(TAG,"getItem(...) fragment : ${mFragmentList.get(position)}")
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun destroyItem(collection: View, position: Int, view: Any) {
        (collection as ViewPager).removeView(view as View)
    }
}