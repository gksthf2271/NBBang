package com.khs.nbbang.page.pager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khs.nbbang.base.BaseFragment

class CustomViewPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    fragments: MutableList<BaseFragment>
) : FragmentStateAdapter(fm, lifecycle) {
    val TAG = this.javaClass.name
    private val mFragmentList: MutableList<BaseFragment> = fragments
    private val mFragmentManager: FragmentManager = fm

    fun getViewPageList(): MutableList<BaseFragment> {
        return mFragmentList
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.v(TAG, "createFragment(...) fragment : ${mFragmentList.get(position)}")
        return mFragmentList.get(position)
    }
}