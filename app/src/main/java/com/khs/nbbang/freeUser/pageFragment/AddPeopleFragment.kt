package com.khs.nbbang.freeUser.pageFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPeopleBinding
import com.khs.nbbang.freeUser.adapter.AddPeopleViewAdapter
import com.khs.nbbang.freeUser.viewModel.PageViewModel
import com.khs.nbbang.page.ItemObj.People


class AddPeopleFragment : BaseFragment() {
    lateinit var mBinding : FragmentAddPeopleBinding
    lateinit var mGridViewAdapter : AddPeopleViewAdapter
    val DEFAULT_SIZE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = ViewModelProvider(
            requireActivity(),
            PageViewModel.PageViewModelFactory(
                requireActivity().supportFragmentManager,
                requireActivity().application
            )
        ).get(PageViewModel::class.java)

        initView()
        observer()
    }

    fun initView() {
        mGridViewAdapter = AddPeopleViewAdapter(requireContext(), mutableListOf())
        mBinding.viewGrid.adapter = mGridViewAdapter
        updateCircle()
        mBinding.viewGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) mGridViewAdapter.addItem(
                mGridViewAdapter.count,
                People(mGridViewAdapter.count, "")
            )
        }
    }

    fun observer() {
        mBinding.viewModel.let {
            it!!._counter.observe(requireActivity(), Observer {
                Log.v(TAG,"observer, call updateCircle(...)")
                updateCircle(it!!)
            })
        }
    }

    fun updateCircle() {
        updateCircle(0)
    }

    fun updateCircle(count: Int) {
        Log.v(TAG,"updateCircle, count :$count")
        mGridViewAdapter.mItemList.clear()
        var dummyPeople = People(0," + ")
        mGridViewAdapter.addItem(dummyPeople)
        for (index in DEFAULT_SIZE .. count) {
            mGridViewAdapter.addItem(index, People(index, ""))
        }
    }
}