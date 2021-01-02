package com.khs.nbbang.freeUser.pageFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.databinding.FragmentSelectPeopleBinding
import com.khs.nbbang.freeUser.adapter.SelectPeopleAdapter
import com.khs.nbbang.freeUser.viewModel.PageViewModel
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.utils.DisplayUtils

class SelectPeopleDialogFragment : DialogFragment() {
    val TAG = this.javaClass.name
    lateinit var mBinding: FragmentSelectPeopleBinding
    lateinit var mGridViewAdapter : SelectPeopleAdapter

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
        mBinding.viewModel = ViewModelProvider(
            requireActivity(),
            PageViewModel.PageViewModelFactory(
                requireActivity().supportFragmentManager,
                requireActivity().application
            )
        ).get(PageViewModel::class.java)
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
        params?.height = (deviceeHeight * 0.95).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    fun initView() {
        Log.v(TAG,"initView(...)")
        mGridViewAdapter = SelectPeopleAdapter(requireContext(), mutableListOf(), mCheckedChangeListener)
        mBinding.viewGrid.adapter = mGridViewAdapter

        mBinding.btnClose.setOnClickListener {
            dismiss()
        }

        mBinding.viewModel.let {
            it!!._peopleList.observe(requireActivity(), Observer {
                if (isAdded) {
                    for (people in it) {
                        addPeopleView(people)
                    }
                }
            })
        }
    }

    fun addPeopleView(people: People){
        Log.v(TAG,"peopleName : ${people.mName}")
        mGridViewAdapter.addItem(mGridViewAdapter.count, people)
    }

    val mCheckedChangeListener = CompoundButton.OnCheckedChangeListener { group, checkedId ->
        Log.v(TAG, "checkedChangeListener(...),  : $checkedId , ${group.text}")
        mBinding.viewModel.let {
            it!!.selectPeopleList(checkedId, tag!!.toInt(), People(group.text.toString()))
            Log.v(TAG,"TEST, ${it._selectedPeopleMap.value!!.get(tag!!.toInt())!!.mPeopleList}")
        }
    }
}