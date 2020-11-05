package com.khs.nbbang.page

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class CustomViewPagerAdapter() : PagerAdapter() {

    lateinit var mContext: Context
    lateinit var mViewList: MutableList<View>

    constructor(context: Context, viewList: MutableList<View>) : this() {
        this.mContext = context
        this.mViewList = viewList
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        var view: View? = null
        view = mViewList.get(position)
        collection.addView(view)
        return view!!
    }

    override fun getCount(): Int {
        return mViewList.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun destroyItem(collection: View, position: Int, view: Any) {
        (collection as ViewPager).removeView(view as View)
    }
}