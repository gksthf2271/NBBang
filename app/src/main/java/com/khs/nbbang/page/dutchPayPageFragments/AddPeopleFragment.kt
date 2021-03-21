package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.sdk.talk.TalkApiClient
import com.khs.nbbang.MainActivity
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddPeopleBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.page.FloatingButtonBaseFragment
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member
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

    override fun add(member: Member?) {
        mPageViewModel.let {
            if (!it!!.addJoinPeople(member ?: return))
                Toast.makeText(context, "${member.name}은 이미 추가된 멤버입니다.", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, "멤버가 추가 되었습니다.", Toast.LENGTH_SHORT).show()

        }
    }

    override fun delete() {
        mPageViewModel.let{
            it!!.deleteJoinPeople(mPageViewModel.mSelectJoinPeople.value ?: return)
        }
    }

    override fun update(member: Member) {
        mPageViewModel.updateJoinPeople(member)
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    companion object class AddPeopleContentsFragment : BaseFragment() {
        lateinit var mAddPeopleContentsBinding: FragmentAddPeopleBinding
        lateinit var mRecyclerViewAdapter: AddPeopleRecyclerViewAdapter
        private val mPageViewModel: PageViewModel by sharedViewModel()
        private val mMemberViewModel: MemberManagementViewModel by sharedViewModel()
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
            mAddPeopleContentsBinding = DataBindingUtil.bind(view)!!
            mAddPeopleContentsBinding.viewModel = mPageViewModel
        }

        override fun onPause() {
            super.onPause()
            if ((requireActivity() as MainActivity).isRunningActivity()) {
                mParentFragment.hideAnyView()
            }
        }

        override fun makeCustomLoadingView(): Dialog? {
            Log.v(TAG,"makeCustomLoadingView(...)")
            return null
        }

        fun initView(parentFragment: AddPeopleFragment) {
            mMemberViewModel.showMemberList()
            mParentFragment = parentFragment

            if (mAddPeopleContentsBinding.recyclerView.adapter == null) {
                mRecyclerViewAdapter =
                    AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                        Log.v(TAG, "ItemClicked, member : ${it.second}")
                        mAddPeopleContentsBinding.viewModel ?: return@AddPeopleRecyclerViewAdapter
                        mAddPeopleContentsBinding.viewModel!!.selectPeople(it.second)
                        mParentFragment.showMemberView()
                    }

                mAddPeopleContentsBinding.recyclerView.apply {
                    layoutManager =
                        GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                    isFocusable = true
                    descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                    adapter = mRecyclerViewAdapter
                }

                mParentFragment.setViewModel(mMemberViewModel)
            }
            addObserver()
        }

        fun addObserver() {
            Log.v(TAG,"addObserver(...)")
            mAddPeopleContentsBinding.viewModel.let {
                it!!.mNBBLiveData.observe(requireActivity(), Observer {
                    Log.v(TAG, "observer, call updateCircle(...) joinPeopleCount : ${it!!.mMemberCount}")
                    var newMemberArrayList = arrayListOf<Member>()
                    newMemberArrayList.addAll(it!!.mMemberList)
                    mRecyclerViewAdapter.setItemList(newMemberArrayList)
                    if (isResumed) {
                        if (mAddPeopleContentsBinding.motionLayout.progress == 1f) mAddPeopleContentsBinding.motionLayout.transitionToEnd()
                        (mAddPeopleContentsBinding.recyclerView.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
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
                    mAddPeopleContentsBinding.rowFavoriteMember.visibility = View.GONE
                    return@Observer
                }
                mAddPeopleContentsBinding.rowFavoriteMember.initView(mPageViewModel)
                mAddPeopleContentsBinding.rowFavoriteMember.setTitle("MEMBER")
                mAddPeopleContentsBinding.rowFavoriteMember.setList(it)

                //TEST Group
                mAddPeopleContentsBinding.rowFavoriteGroup.initView(mPageViewModel)
                mAddPeopleContentsBinding.rowFavoriteGroup.setTitle("GROUP")
                mAddPeopleContentsBinding.rowFavoriteGroup.setList(it)
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