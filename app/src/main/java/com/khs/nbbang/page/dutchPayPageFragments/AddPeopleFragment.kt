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
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member
import kotlinx.android.synthetic.main.cview_edit_people.view.*
import kotlinx.android.synthetic.main.fragment_add_people.view.*
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

    override fun add(obj: Member?) {
        mPageViewModel.let {
            it!!.addJoinPeople(obj ?: return)
        }
    }

    override fun delete() {
        mPageViewModel.let{
            it!!.deleteJoinPeople(mPageViewModel.mSelectJoinPeople.value ?: return)
        }
    }

    override fun update(name: String, description: String, resId: Int) {
        mPageViewModel.updateJoinPeople(name, description, resId)
    }


    companion object class AddPeopleContentsFragment : BaseFragment() {
        lateinit var mBinding: FragmentAddPeopleBinding
        lateinit var mRecyclerViewAdapter: AddPeopleRecyclerViewAdapter
        private val mPageViewModel: PageViewModel by sharedViewModel()
        private val mMemberViewModel: MemberManagementViewModel by sharedViewModel()

        private var mMemberList = arrayListOf<Member>()
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
//            var peopleListBuffer = mutableListOf<Member>()
//
//            for (index in 0 until mRecyclerViewAdapter.itemCount) {
//                try {
//                    peopleListBuffer.add(
//                        Member(
//                            -1,
//                            index,
//                            mBinding.recyclerView.getChildAt(index).txt_name.text.toString(),
//                            0,
//                            "",
//                            0
//                        )
//                    )
//                } catch (e: Exception) {
//                    Log.e(TAG, "$e,\n\n $index")
//                }
//            }
//
//            if (isUpdatedPeople(peopleListBuffer)) {
//                Log.v(TAG, "isUpdatedPeople, false")
//            } else {
//                Log.v(TAG, "isUpdatedPeople, true")
//                mBinding.viewModel.let { it!!.updatePeopleList(peopleListBuffer) }
//            }

            mParentFragment.hideMemeberView()
            mParentFragment.hideAddMemberView()
        }

        private fun isUpdatedPeople(memberList: MutableList<Member>): Boolean {
            mBinding.viewModel.let {
                if (memberList.size == it!!.mNBBLiveData.value!!.mMemberList.size) {
                    for (index in 0 until memberList.size) {
                        if (!memberList.get(index).name.equals(
                                it!!.mNBBLiveData.value!!.mMemberList.get(
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
            mMemberViewModel.showMemberList()
            mParentFragment = parentFragment
            mRecyclerViewAdapter = AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                Log.v(TAG,"ItemClicked, member : ${it.second}")
                mBinding.viewModel ?: return@AddPeopleRecyclerViewAdapter
                mBinding.viewModel!!.selectPeople(it.second)
                mParentFragment.showMemberView()
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
                    Log.v(TAG, "observer, call updateCircle(...) joinPeopleCount : ${it!!.mMemberCount}")
                    mMemberList.clear()
                    mMemberList.addAll(it!!.mMemberList)
                    mRecyclerViewAdapter.setItemList(this.mMemberList)
                    if (isResumed) {
                        if (mBinding.motionLayout.progress == 1f) mBinding.motionLayout.transitionToEnd()
                        (mBinding.recyclerView.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
                            mRecyclerViewAdapter.itemCount - 1, mRecyclerViewAdapter.itemCount - 1
                        )
                    }
                })

                it!!.mSelectJoinPeople.observe(requireActivity(), Observer {
                    Log.v(TAG, "Select JoinPeople : $it")
                    it ?: return@Observer
                    mParentFragment.selectMember(it)
                })
            }

            mMemberViewModel.mMemberList.observe(requireActivity(), Observer {
                if (it.isEmpty()) {
                    mBinding.rowFavoriteMember.visibility = View.GONE
                    return@Observer
                }
                mBinding.rowFavoriteMember.initView(mPageViewModel)
                mBinding.rowFavoriteMember.setTitle("MEMBER")
                mBinding.rowFavoriteMember.setList(it)

                //TEST Group
                mBinding.rowFavoriteGroup.initView(mPageViewModel)
                mBinding.rowFavoriteGroup.setTitle("GROUP")
                mBinding.rowFavoriteGroup.setList(it)
            })
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