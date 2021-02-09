package com.khs.nbbang.base

import androidx.viewpager.widget.PagerAdapter
import com.khs.nbbang.page.pageFragment.AddPeopleFragment
import com.khs.nbbang.page.pageFragment.AddPlaceFragment
import com.khs.nbbang.page.pageFragment.PeopleCountFragment
import com.khs.nbbang.page.pageFragment.ResultPageFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter

open class PageActivity : BaseActivity() {
    val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    fun getPageAdapter() : PagerAdapter{
        return CustomViewPagerAdapter(
            supportFragmentManager,
            mPageViewList
        )
    }
}