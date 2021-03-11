package com.khs.nbbang.page.dutchPayPageFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentSelectPeopleBinding
import com.khs.nbbang.page.adapter.SelectPeopleRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.page.viewModel.SelectMemberViewModel
import com.khs.nbbang.user.Member
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SelectPeopleDialogFragment : BaseDialogFragment(){
    val TAG = this.javaClass.simpleName
    lateinit var mBinding: FragmentSelectPeopleBinding
    lateinit var mRecyclerViewAdapter: SelectPeopleRecyclerViewAdapter
    private val mPageViewModel : PageViewModel by sharedViewModel()
    private val mSelectMemberViewModel : SelectMemberViewModel by sharedViewModel()

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
        mBinding.viewModel = mPageViewModel
        initView()
    }

    override fun onResume() {
        CURRENT_DIALOG_TYPE = DIALOG_TYPE.TYPE_SELECT_PEOPLE_FROM_ADD_PLACE_DIALOG
        super.onResume()
    }

    fun initView() {
        Log.v(TAG,"initView(...), TAG : $tag")
        mRecyclerViewAdapter = SelectPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
            Log.v(TAG, "ItemClicked, member : ${it.second}")
        }

        mBinding.recyclerView.apply {
            layoutManager =
                GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            isFocusable = true
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            adapter = mRecyclerViewAdapter
        }

        mBinding.btnClose.setOnClickListener {
            dismiss()
        }

        mBinding.btnSave.setOnClickListener {
            mBinding.viewModel.let {pageViewModel ->
                mBinding.viewModel!!.saveSelectedPeople(tag!!.toInt(), mSelectMemberViewModel.getSelectedMemberList())
                mSelectMemberViewModel.clearSelectedMemberList()
            }
            dismiss()
        }

        mBinding.viewModel.let {
            it!!.mNBBLiveData.observe(requireActivity(), Observer {
                if (isAdded) {
                    var allPairList : ArrayList<Pair<Member, Boolean>> = arrayListOf()
                    for (member in it!!.mMemberList) {
                        allPairList.add(Pair(member,false))
                    }
                    mRecyclerViewAdapter.setItemList(allPairList)
                }
            })

            it.mSelectedPeopleMap?.observe(requireActivity(), Observer {
                Log.v(TAG,"observer(...)")
                if (isAdded) {
                it ?: return@Observer
                tag ?: return@Observer
                    it!!.get(tag!!.toInt()).let { nbb ->
                        nbb ?: return@Observer
                        var selectedPairList : ArrayList<Pair<Member, Boolean>> = arrayListOf()
                        for (member in nbb!!.mMemberList) {
                            selectedPairList.add(Pair(member,true))
                        }
                        mRecyclerViewAdapter.setSelectedMemberList(selectedPairList)
                    }
                }
            })

            mSelectMemberViewModel.gSelectedMemberList.observe(requireActivity(), Observer {
                Log.v(TAG,"selectedList : ${it!!}")
            })
        }
    }
}