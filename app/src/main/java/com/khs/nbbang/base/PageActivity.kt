package com.khs.nbbang.base

import androidx.viewpager.widget.PagerAdapter
import com.khs.nbbang.freeUser.pageFragment.AddPeopleFragment
import com.khs.nbbang.freeUser.pageFragment.AddPlaceFragment
import com.khs.nbbang.freeUser.pageFragment.PeopleCountFragment
import com.khs.nbbang.freeUser.pageFragment.ResultPageFragment
import com.khs.nbbang.page.CustomViewPagerAdapter

open class PageActivity : BaseActivity() {
    val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    fun getPageAdapter() : PagerAdapter{
        return CustomViewPagerAdapter(supportFragmentManager, mPageViewList)
    }
}