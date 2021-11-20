package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.databinding.FragmentAddPeopleBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.page.FloatingButtonBaseFragment
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member
import kotlinx.android.synthetic.main.fragment_add_people.*
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
            if (!it.addJoinPeople(member ?: return))
                Toast.makeText(context, "${member.name}은 이미 추가된 멤버입니다.", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, "멤버가 추가 되었습니다.", Toast.LENGTH_SHORT).show()

        }
    }

    override fun delete() {
        mPageViewModel.let{
            it.deleteJoinPeople(mPageViewModel.mSelectJoinPeople.value ?: return)
        }
    }

    override fun update(member: Member) {
        mPageViewModel.updateJoinPeople(member)
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (isShownMemberView()) {
                    hideAnyView()
                    return true
                }
            }
        }
        return false
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
            mMemberViewModel.showFavoriteMemberListByType(MemberType.TYPE_FREE_USER)
            mMemberViewModel.showFavoriteMemberListByType(MemberType.TYPE_KAKAO)
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

        private fun addObserver() {
            Log.v(TAG,"addObserver(...)")
            mAddPeopleContentsBinding.viewModel?.let {
                it.mNBBLiveData.observe(requireActivity(), Observer {
                    Log.v(TAG, "observer, call updateCircle(...) joinPeopleCount : ${it.mMemberCount}")
                    val newMemberArrayList = arrayListOf<Member>()
                    newMemberArrayList.addAll(it.mMemberList)
                    mRecyclerViewAdapter.setItemList(newMemberArrayList)
                })

                it.mSelectJoinPeople.observe(requireActivity(), Observer {
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

                mAddPeopleContentsBinding.groupFavorite.visibility = View.VISIBLE
                mAddPeopleContentsBinding.rowFavoriteMember.initView(mPageViewModel)
                mAddPeopleContentsBinding.rowFavoriteMember.setControlScrolling()
                mAddPeopleContentsBinding.rowFavoriteMember.setTitle("LOCAL MEMBER")
                mAddPeopleContentsBinding.rowFavoriteMember.setList(it)
            })

            mMemberViewModel.gKakaoFriendList.observe(requireActivity(), Observer {
                if (it.isEmpty()) {
                    mAddPeopleContentsBinding.rowFavoriteMember.visibility = View.GONE
                    return@Observer
                }

                mAddPeopleContentsBinding.groupFavorite.visibility = View.VISIBLE
                mAddPeopleContentsBinding.rowFavoriteGroup.initView(mPageViewModel)
                mAddPeopleContentsBinding.rowFavoriteMember.setControlScrolling()
                mAddPeopleContentsBinding.rowFavoriteGroup.setTitle("KAKAO MEMBER")
                mAddPeopleContentsBinding.rowFavoriteGroup.setList(it)
            })
        }

        override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
            when(keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    //todo 팝업 기능 추가 시 hide는 여기서 처리
                }
            }
            return false
        }
    }
}