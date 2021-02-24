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
import com.kakao.sdk.talk.TalkApiClient
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPeopleBinding
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import kotlinx.android.synthetic.main.cview_edit_people.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class AddPeopleFragment : BaseFragment(){
    lateinit var mBinding : FragmentAddPeopleBinding
    lateinit var mRecyclerViewAdapter : AddPeopleRecyclerViewAdapter
    val mViewModel : PageViewModel by sharedViewModel()
    private val DEFAULT_SIZE = 1
    private var mPeopleList = mutableListOf<People>()

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
        loadFriendsListWithKakao()
    }

    override fun onPause() {
        super.onPause()
        var peopleListBuffer = mutableListOf<People>()

        for (index in DEFAULT_SIZE until mBinding.recyclerView.childCount) {
            try {
                peopleListBuffer.add(People(index, mBinding.recyclerView.getChildAt(index).txt_name.text.toString()))
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

    private fun isUpdatedPeople(peopleList: MutableList<People>) : Boolean{
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
        mRecyclerViewAdapter = AddPeopleRecyclerViewAdapter(requireContext(), mPeopleList) {
            if (it.first == 0) {
                mPeopleList.add(People(mPeopleList.size, ""))
                mBinding.viewModel.let {
                    it!!.setPeopleCount(mRecyclerViewAdapter.mItemList.size - DEFAULT_SIZE)
                }
            } else {

            }
        }

        mBinding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            isFocusable = true
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            adapter = mRecyclerViewAdapter
        }
        initCircle()
    }

    fun observer() {
        mBinding.viewModel.let {
            it!!.mNBBLiveData.observe(requireActivity(), Observer {
                Log.v(TAG,"observer, call updateCircle(...)")
                updateCircle(it!!)
            })
        }
    }

    private fun initCircle() {
        mPeopleList.clear()
        var dummyPeople = People(0," + ")
        mPeopleList.add(dummyPeople)
        mRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun updateCircle(NBB: NBB) {
        Log.v(TAG,"updateCircle, count :${NBB.mPeopleCount}")
        initCircle()
        for (index in DEFAULT_SIZE .. NBB.mPeopleCount) {
            try {
                val people = People(index,"")
                mPeopleList.add(index, people)
                mRecyclerViewAdapter.notifyDataSetChanged()
            } catch (IOOB: IndexOutOfBoundsException) {
                Log.v(TAG,"$IOOB")
            }
        }
    }

    private fun loadFriendsListWithKakao() {
        // 카카오톡 친구 목록 가져오기 (기본)
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
            }
            else if (friends != null) {
                Log.i(TAG, "카카오톡 친구 목록 가져오기 성공 \n${friends.elements.joinToString("\n")}")

                // 친구의 UUID 로 메시지 보내기 가능
            }
        }
    }
}