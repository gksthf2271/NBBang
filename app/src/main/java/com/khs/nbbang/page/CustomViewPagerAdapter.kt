package com.khs.nbbang.page

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class CustomViewPagerAdapter(fm: FragmentManager, fragments: MutableList<Fragment>) : FragmentStatePagerAdapter(fm) {
    var mFragmentList: MutableList<Fragment>
    var mFragmentManager : FragmentManager

    init {
        mFragmentList = fragments
        mFragmentManager = fm
    }

    fun getViewPageList() : MutableList<Fragment> {
        return mFragmentList
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        fragment = mFragmentList.get(position)
        return fragment
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun destroyItem(collection: View, position: Int, view: Any) {
        (collection as ViewPager).removeView(view as View)
    }
}