package com.khs.nbbang.page.dutchPayPageFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentSelectPeopleBinding
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.adapter.SelectPeopleAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SelectPeopleDialogFragment : BaseDialogFragment(){
    val TAG = this.javaClass.name
    lateinit var mBinding: FragmentSelectPeopleBinding
    lateinit var mGridViewAdapter : SelectPeopleAdapter
    val mViewModel : PageViewModel by sharedViewModel()

    companion object {
        @Volatile
        private var instance: SelectPeopleDialogFragment? = null

        @JvmStatic
        fun getInstance(): SelectPeopleDialogFragment =
            instance ?: synchronized(this) {
                instance
                    ?: SelectPeopleDialogFragment().also {
                        instance = it
                    }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG,"onViewCreated(...)")
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        initView()
    }

    override fun onResume() {
        CURRENT_DIALOG_TYPE = DIALOG_TYPE.TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG
        super.onResume()
    }
    fun initView() {
        Log.v(TAG,"initView(...), TAG : $tag")
        mGridViewAdapter = SelectPeopleAdapter(requireContext(), mutableListOf())
        mBinding.viewGrid.adapter = mGridViewAdapter

        mBinding.btnClose.setOnClickListener {
            dismiss()
        }

        mBinding.btnSave.setOnClickListener {
            mBinding.viewModel.let {
                it!!.saveSelectedPeople(tag!!.toInt(), mGridViewAdapter.getSelectedPeopleList())
            }
            dismiss()
        }

        mBinding.viewModel.let {
            it!!.mNBBLiveData.observe(requireActivity(), Observer {
                if (isAdded) {
                    for (people in it.mPeopleList) {
                        addPeopleView(people)
                    }
                }
            })

            it.mSelectedPeopleMap.value!!.get(tag!!.toInt())?.let {
                mGridViewAdapter.setSelectPeople(it!!)
            }
        }
    }

    fun addPeopleView(people: People){
        Log.v(TAG,"peopleName : ${people.name}")
        Log.v(TAG,"peopleNameView Index : ${mGridViewAdapter.count}")
        mGridViewAdapter.addItem(mGridViewAdapter.count, people)
    }
}