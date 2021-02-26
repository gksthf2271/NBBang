package com.khs.nbbang.page.dutchPayPageFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.talk.TalkApiClient
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPeopleBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.page.FloatingButtonBaseFragment
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.User
import kotlinx.android.synthetic.main.cview_edit_people.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddPeopleFragment : FloatingButtonBaseFragment() {
    private val mPageViewModel: PageViewModel by sharedViewModel()
    private val mAddPeopleContentsFragment by lazy {
        AddPeopleContentsFragment()
    }

    override fun makeContentsFragment(): Fragment? {
        return mAddPeopleContentsFragment
    }

    override fun init() {
        mAddPeopleContentsFragment.initView(this)
    }


    //TODO :: Member, People 정리 필요 현재까지는 여기까지 정상작동하지만, line 44에서 return 
    override fun add(obj: User?) {
        var people = obj as? People
        people ?: return
        mPageViewModel.addPeople(people)
    }

    override fun delete() {

    }

    override fun update(old: User, new: User) {
    }


    companion object class AddPeopleContentsFragment : BaseFragment() {
        lateinit var mBinding: FragmentAddPeopleBinding
        lateinit var mRecyclerViewAdapter: AddPeopleRecyclerViewAdapter
        private val mPageViewModel: PageViewModel by sharedViewModel()
        private val mMemberViewModel: MemberManagementViewModel by sharedViewModel()

        private var mPeopleList = arrayListOf<People>()
        private lateinit var mParentFragment: AddPeopleFragment

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
            mBinding.viewModel = mPageViewModel
        }

        override fun onPause() {
            //TODO : 성능저하 요소 리팩토링 필요
            super.onPause()
            var peopleListBuffer = mutableListOf<People>()

            for (index in 0 until mBinding.recyclerView.childCount) {
                try {
                    peopleListBuffer.add(
                        People(
                            index,
                            mBinding.recyclerView.getChildAt(index).txt_name.text.toString()
                        )
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "$e,\n\n $index")
                }
            }

            if (isUpdatedPeople(peopleListBuffer)) {
                Log.v(TAG, "isUpdatedPeople, false")
                mBinding.viewModel.let { it!!.updatePeopleList(peopleListBuffer) }
            } else {
                Log.v(TAG, "isUpdatedPeople, true")
            }
        }

        private fun isUpdatedPeople(peopleList: MutableList<People>): Boolean {
            mBinding.viewModel.let {
                if (peopleList.size == it!!.mNBBLiveData.value!!.mPeopleList.size) {
                    for (index in 0 until peopleList.size) {
                        if (!peopleList.get(index).name.equals(
                                it!!.mNBBLiveData.value!!.mPeopleList.get(
                                    index
                                ).name
                            )
                        ) {
                            return false
                        }
                    }
                    return true
                } else {
                    return false
                }
            }
        }

        fun initView(parentFragment: AddPeopleFragment) {
            mParentFragment = parentFragment
            mRecyclerViewAdapter = AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                if (it.first == 0) {
                    mPeopleList.add(People(mPeopleList.size, ""))
                    mBinding.viewModel.let {
                        it!!.setPeopleCount(mRecyclerViewAdapter.mItemList.size)
                    }
                }
            }

            mBinding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                isFocusable = true
                descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                adapter = mRecyclerViewAdapter
            }

            mParentFragment.setViewModel(mMemberViewModel)
            observer()
        }

        fun observer() {
            mBinding.viewModel.let {
                it!!.mNBBLiveData.observe(requireActivity(), Observer {
                    Log.v(TAG, "observer, call updateCircle(...) peoplecount : ${it!!.mPeopleCount}")
                    mPeopleList.clear()
                    for (people in it!!.mPeopleList) {
                        try {
                            val people = People(people.index, people.name)
                            this.mPeopleList.add(people)
                        } catch (IOOB: IndexOutOfBoundsException) {
                            Log.e(TAG, "$IOOB")
                        }
                    }
                    mRecyclerViewAdapter.setItemList(this.mPeopleList)
                })
            }
        }

        private fun loadFriendsListWithKakao() {
            // 카카오톡 친구 목록 가져오기 (기본)
            TalkApiClient.instance.friends { friends, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡 친구 목록 가져오기 실패", error)
                } else if (friends != null) {
                    Log.i(TAG, "카카오톡 친구 목록 가져오기 성공 \n${friends.elements.joinToString("\n")}")

                    // 친구의 UUID 로 메시지 보내기 가능
                }
            }
        }
    }
}