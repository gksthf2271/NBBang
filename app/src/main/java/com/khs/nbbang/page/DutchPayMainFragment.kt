package com.khs.nbbang.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentPageHomeBinding
import com.khs.nbbang.page.dutchPayPageFragments.AddPeopleFragment
import com.khs.nbbang.page.dutchPayPageFragments.AddPlaceFragment
import com.khs.nbbang.page.dutchPayPageFragments.PeopleCountFragment
import com.khs.nbbang.page.dutchPayPageFragments.ResultPageFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DutchPayMainFragment : BaseFragment() {
    lateinit var mBinding : FragmentPageHomeBinding
    val mViewModel: PageViewModel by sharedViewModel()

    val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        PeopleCountFragment(),
        AddPeopleFragment(),
        AddPlaceFragment(),
        ResultPageFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        initView()
    }

    fun initView() {
        mBinding.viewPager.adapter = CustomViewPagerAdapter(
            requireActivity().supportFragmentManager,
            mPageViewList
        )
        mBinding.viewPager.currentItem = 0
        mBinding.viewPager.setPagingEnable(false)
        mBinding.viewPager.offscreenPageLimit = mPageViewList.size - 2
        mBinding.viewIndicator.setViewPager(mBinding.viewPager)
    }
}