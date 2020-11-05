package com.khs.nbbang.page

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.page.customView.AddPeopleView
import com.khs.nbbang.page.customView.AddPlaceView
import com.khs.nbbang.page.customView.PeopleCountView
import com.khs.nbbang.page.customView.ResultPageView
import kotlinx.android.synthetic.main.fragment_freeuser.*

class FreeUserFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_freeuser, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        initView()
        view_pager.setPagingEnable(false)
    }

    fun initView() {

        val pageViewList : MutableList<View> = mutableListOf(
            PeopleCountView(requireContext()),
            AddPeopleView(requireContext()),
            AddPlaceView(requireContext()),
            ResultPageView(requireContext()))

        view_pager.adapter =
            CustomViewPagerAdapter(
                requireContext(),
                pageViewList)

        view_pager.currentItem = 0

        view_indicator.setViewPager(view_pager)
    }
}