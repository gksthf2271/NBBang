package com.khs.nbbang.page.dutchPayPageFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPlaceBinding
import com.khs.nbbang.page.adapter.AddPlaceRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member
import org.koin.android.viewmodel.ext.android.sharedViewModel


/**
 * TODO : 출시 전 해야될 것들
 *  1. 갤러리 연동하여, 프로필 설정 할 수 있는 기능 추가                                                    ---> 5순위
 *  2. 카카오 친구목록 UI 구현                                                                        ---> 4순위
 *  3. AddPeopleFragment AddMemberView 출력 시 하단 FavoriteMember 보이고, 선택하여 추가 할 수 있는 기능 구현 ---> 2순위
 *  4. UI 정리(테마 변경)                                                                            ---> 3순위
 *  5. AddPlaceFragment UI 일관성 유지되도록 수정 FloatingButton에 MotionView                           ---> 1순위(완료, 중복소스 존재)
 *  -------------------------------------------- 목표일 !!!!!3월 28일 출시!!!!!!
 *  Group관리 기능은 2.0에 구현
 * **/


class AddPlaceFragment : BaseFragment() {
    lateinit var mBinding: FragmentAddPlaceBinding

    val mViewModel: PageViewModel by sharedViewModel()
    lateinit var mRecyclerViewAdapter : AddPlaceRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel

        initView()
        addObserver()
    }

    override fun onPause() {
        super.onPause()
        mBinding.viewModel.let { pageViewModel ->
            pageViewModel!!.selectPlace(-1)
        }
    }


    fun initView() {
        mRecyclerViewAdapter = AddPlaceRecyclerViewAdapter(requireActivity(), mBinding.viewModel!!, arrayListOf(1),{
            Log.v(TAG,"ItemClicked, index : $it")
            //TODO item remove
        },{
            Log.v(TAG,"JoinBtnClicked, index : $it")
            mBinding.viewModel.let { pageViewModel ->
                pageViewModel!!.selectPlace(it)
            }
            showSelectPeopleDialog(it.toString())
        })

        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(HistoryItemDecoration(10))
            adapter = mRecyclerViewAdapter
        }

        mBinding.btnAdd.setOnClickListener {
            mRecyclerViewAdapter.setItem(mRecyclerViewAdapter.itemCount)
        }
    }

    private fun addObserver() {
        mBinding.viewModel.let { pageViewModel ->
            val viewModel = mBinding.viewModel!!
            pageViewModel!!.mNBBLiveData.observe(requireActivity(), Observer {
                Log.v(TAG, "mNBBLiveData, Observer(...) : $it")
                viewModel.clearSelectedPeople()
            })
        }
    }

    private fun showSelectPeopleDialog(tag: String) {
        Log.v(TAG, "showSelectPeopleDialog(...)")
        var selectPeopleDialog = SelectPeopleDialogFragment.getInstance()
        selectPeopleDialog.show(requireActivity().supportFragmentManager, tag)
    }
}
