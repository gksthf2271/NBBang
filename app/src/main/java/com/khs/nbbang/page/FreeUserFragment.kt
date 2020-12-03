package com.khs.nbbang.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.page.pageView.AddPeopleView
import com.khs.nbbang.page.pageView.AddPlaceView
import com.khs.nbbang.page.pageView.PeopleCountView
import com.khs.nbbang.page.pageView.ResultPageView
import com.khs.nbbang.page.viewModel.PageViewModel
import kotlinx.android.synthetic.main.fragment_freeuser.*

class FreeUserFragment : BaseFragment() {
    lateinit var mPageViewModel: PageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_freeuser, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPageViewModel = ViewModelProvider(requireActivity()).get(PageViewModel::class.java)


        initView()
        //Page slide 유무
        view_pager.setPagingEnable(false)
        mPageViewModel.mPeopleCount.observe(requireActivity(), Observer {
            Log.v(TAG,"people count : ${it}")
            mPageViewModel.updatePeopleCircle()
        })

        mPageViewModel.mPeopleListObj.observe(requireActivity(), Observer {

        })
    }

    fun initView() {
        val pageViewList : MutableList<View> = mutableListOf(
            PeopleCountView(requireContext()).apply { setViewModel(mPageViewModel, this@FreeUserFragment) },
            AddPeopleView(requireContext()).apply { setViewModel(mPageViewModel, this@FreeUserFragment) },
            AddPlaceView(requireContext()).apply { setViewModel(mPageViewModel, this@FreeUserFragment) },
            ResultPageView(requireContext()).apply { setViewModel(mPageViewModel, this@FreeUserFragment) }
        )

        view_pager.adapter =
            CustomViewPagerAdapter(
                requireContext(),
                pageViewList)

        view_pager.currentItem = 0

        view_indicator.setViewPager(view_pager)
    }
}