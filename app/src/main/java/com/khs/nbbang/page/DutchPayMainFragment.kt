package com.khs.nbbang.page

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.animation.ZoomOutPageTransformer
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentPageHomeBinding
import com.khs.nbbang.page.dutchPayPageFragments.AddPeopleFragment
import com.khs.nbbang.page.dutchPayPageFragments.AddPlaceFragment
import com.khs.nbbang.page.dutchPayPageFragments.PeopleCountFragment
import com.khs.nbbang.page.dutchPayPageFragments.ResultPageFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import kotlinx.android.synthetic.main.fragment_people_count.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DutchPayMainFragment : BaseFragment() {
    lateinit var mBinding : FragmentPageHomeBinding
    val mViewModel: PageViewModel by sharedViewModel()

    private val mPageViewList: MutableList<BaseFragment> = mutableListOf(
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

    private fun initView() {
        mBinding.viewPager.adapter = CustomViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            mPageViewList
        )
        mBinding.viewPager.currentItem = 0
        mBinding.viewPager.get(0).setOnTouchListener{ _, _ ->
            mBinding.viewModel.let {
                if ( it!!.mNNBLiveData.value!!.mPeopleCount <= 0) {
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
        mBinding.viewPager.offscreenPageLimit = mPageViewList.size - 2
        mBinding.viewPager.setPageTransformer(ZoomOutPageTransformer())
        mBinding.viewIndicator.setViewPager2(mBinding.viewPager)
    }
}