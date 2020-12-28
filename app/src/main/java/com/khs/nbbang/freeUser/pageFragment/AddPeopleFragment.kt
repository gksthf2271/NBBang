package com.khs.nbbang.freeUser.pageFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPeopleBinding
import com.khs.nbbang.freeUser.adapter.AddPeopleViewAdapter
import com.khs.nbbang.freeUser.viewModel.PageViewModel
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.ItemObj.PeopleListObj
import kotlinx.android.synthetic.main.cview_edit_people.view.*
import java.lang.IndexOutOfBoundsException


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

    override fun onPause() {
        super.onPause()
        var peopleListBuffer = mutableListOf<People>()
        for (index in DEFAULT_SIZE .. mBinding.viewGrid.childCount - 1 ) {
            try {
                peopleListBuffer.add(People(index, mBinding.viewGrid.getChildAt(index).txt_name.text.toString()))
            } catch (e:Exception) {
                Log.e(TAG,"$e,\n\n $index")
            }
        }
        mBinding.viewModel.let { it!!.updatePeopleList(peopleListBuffer) }
    }

    fun initView() {
        mGridViewAdapter = AddPeopleViewAdapter(requireContext(), mutableListOf())
        mBinding.viewGrid.adapter = mGridViewAdapter
        initCircle()
        mBinding.viewGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                mGridViewAdapter.addItem(
                    mGridViewAdapter.count,
                    People(mGridViewAdapter.count, "")
                )
                mBinding.viewModel.let {
                    it!!._counter.value = mGridViewAdapter.mItemList.size - DEFAULT_SIZE
                }
            }
        }
    }

    fun observer() {
        mBinding.viewModel.let {
            it!!._counter.observe(requireActivity(), Observer {
                Log.v(TAG,"observer, call updateCircle(...)")
                updateCircle(mBinding.viewModel.let {
                    it!!._peopleListLiveData.value!! })
            })
        }
    }

    fun initCircle() {
        mGridViewAdapter.mItemList.clear()
        var dummyPeople = People(0," + ")
        mGridViewAdapter.addItem(dummyPeople)
    }

    fun updateCircle(peopleListObj: PeopleListObj) {
        Log.v(TAG,"updateCircle, count :${peopleListObj.mPeopleCount}")
        initCircle()
        for (index in DEFAULT_SIZE .. peopleListObj.mPeopleCount) {
            try {
                mGridViewAdapter.addItem(index, People(index," "))
            } catch (IOOB: IndexOutOfBoundsException) {
                Log.v(TAG,"$IOOB")
            }
        }
    }
}