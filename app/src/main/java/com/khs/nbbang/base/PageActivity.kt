package com.khs.nbbang.base

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khs.nbbang.page.dutchPayPageFragments.AddPeopleFragment
import com.khs.nbbang.page.dutchPayPageFragments.AddPlaceFragment
import com.khs.nbbang.page.dutchPayPageFragments.PeopleCountFragment
import com.khs.nbbang.page.dutchPayPageFragments.ResultPageFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter

open class PageActivity : BaseActivity() {
    val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    fun getPageAdapter() : FragmentStateAdapter{
        return CustomViewPagerAdapter(
            supportFragmentManager,
            lifecycle,
            mPageViewList
        )
    }
}