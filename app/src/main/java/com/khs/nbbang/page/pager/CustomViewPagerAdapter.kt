package com.khs.nbbang.page.pager

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.utils.LogUtil

class CustomViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    fragments: MutableList<BaseFragment>
) : FragmentStateAdapter(fm, lifecycle) {
    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_UI
    private val mFragmentList: MutableList<BaseFragment> = fragments

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "createFragment(...) fragment : ${mFragmentList[position]}")
        return mFragmentList[position]
    }

    fun getFragmentPointColor(position: Int) : Int {
        return when(position) {
            1 -> {
                Color.parseColor("#80000000")
            }
            else -> {
                Color.parseColor("#00000000")
            }
        }
    }
}