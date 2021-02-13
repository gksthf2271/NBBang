package com.khs.nbbang.page.dutchPayPageFragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.databinding.FragmentSelectPeopleBinding
import com.khs.nbbang.page.adapter.SelectPeopleAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.utils.DisplayUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SelectPeopleDialogFragment : DialogFragment(){
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
        super.onResume()
        resizeDialog()
        setBackgroundColorDialog()
    }

    fun setBackgroundColorDialog() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    fun resizeDialog(){
        val size = DisplayUtils().getDisplaySize(requireContext())
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceeHeight = size.y
        params?.width = (deviceWidth * 0.95).toInt()
        params?.height = (deviceeHeight * 0.75).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
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
            it!!.mNNBLiveData.observe(requireActivity(), Observer {
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
        Log.v(TAG,"peopleName : ${people.mName}")
        Log.v(TAG,"peopleNameView Index : ${mGridViewAdapter.count}")
        mGridViewAdapter.addItem(mGridViewAdapter.count, people)
    }
}