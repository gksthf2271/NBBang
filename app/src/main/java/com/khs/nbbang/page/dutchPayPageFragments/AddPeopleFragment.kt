package com.khs.nbbang.page.dutchPayPageFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.PeopleNameWatcherCallback
import com.khs.nbbang.page.adapter.AddPeopleViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import com.khs.nbbang.databinding.FragmentAddPeopleBinding
import kotlinx.android.synthetic.main.cview_edit_people.view.*


class AddPeopleFragment : BaseFragment() {
    lateinit var mBinding : FragmentAddPeopleBinding
    lateinit var mGridViewAdapter : AddPeopleViewAdapter
    val mViewModel : PageViewModel by sharedViewModel()
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
        mBinding.viewModel = mViewModel
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
        if (isUpdatedPeople(peopleListBuffer)) {
            Log.v(TAG,"isUpdatedPeople, true")
            mBinding.viewModel.let { it!!.updatePeopleList(peopleListBuffer) }
        } else {
            Log.v(TAG,"isUpdatedPeople, false")
        }
    }

    fun isUpdatedPeople(peopleList: MutableList<People>) : Boolean{
        mBinding.viewModel.let {
            if (peopleList.size == it!!.mNBBLiveData.value!!.mPeopleList.size) {
                for (index in 0 until peopleList.size) {
                    if (!peopleList.get(index).name.equals(it!!.mNBBLiveData.value!!.mPeopleList.get(index).name)){
                        return true
                    }
                }
                return false
            } else {
                return true
            }
        }
    }

    fun initView() {
        mGridViewAdapter = AddPeopleViewAdapter(requireContext(), mutableListOf(), object :
            PeopleNameWatcherCallback {
            override fun onCallback(peopleId: Int, name: String) {
//                mBinding.viewModel.let {
//                    it!!.savePeopleName(peopleId - DEFAULT_SIZE, name)
//                }
            }
        })
        mBinding.viewGrid.adapter = mGridViewAdapter
        initCircle()
        mBinding.viewGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                mGridViewAdapter.addItem(
                    mGridViewAdapter.count,
                    People(mGridViewAdapter.count, "")
                )
                mBinding.viewModel.let {
                    it!!.setPeopleCount(mGridViewAdapter.mItemList.size - DEFAULT_SIZE)
                }
            }
        }
    }

    fun observer() {
        mBinding.viewModel.let {
            it!!.mNBBLiveData.observe(requireActivity(), Observer {
                Log.v(TAG,"observer, call updateCircle(...)")
                updateCircle(it!!)
            })
        }
    }

    fun initCircle() {
        mGridViewAdapter.mItemList.clear()
        var dummyPeople = People(0," + ")
        mGridViewAdapter.addItem(dummyPeople)
    }

    fun updateCircle(NBB: NBB) {
        Log.v(TAG,"updateCircle, count :${NBB.mPeopleCount}")
        initCircle()
        for (index in DEFAULT_SIZE .. NBB.mPeopleCount) {
            try {
                val people = People(index,"")
                mGridViewAdapter.addItem(index, people)
            } catch (IOOB: IndexOutOfBoundsException) {
                Log.v(TAG,"$IOOB")
            }
        }
    }
}