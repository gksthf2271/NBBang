package com.khs.nbbang.page.dutchPayPageFragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var mAddPeopleContentsBinding: FragmentAddPeopleBinding
    lateinit var mRecyclerViewAdapter: AddPeopleRecyclerViewAdapter
    private val mPageViewModel: PageViewModel by sharedViewModel()
    private val mMemberViewModel: MemberManagementViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAddPeopleContentsBinding = FragmentAddPeopleBinding.inflate(inflater, container, false)
        super.onCreateView(inflater, mAddPeopleContentsBinding.root as ViewGroup, savedInstanceState)
        return mAddPeopleContentsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAddPeopleContentsBinding.viewModel = mPageViewModel
        initView()
    }

    override fun onResume() {
        super.onResume()
        addObserver()
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
        Log.v(TAG_CLASS,"makeCustomLoadingView(...)")
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

    private fun initView() {
        mMemberViewModel.showFavoriteMemberListByType(MemberType.TYPE_FREE_USER)
        mMemberViewModel.showFavoriteMemberListByType(MemberType.TYPE_KAKAO)

        if (mAddPeopleContentsBinding.recyclerView.adapter == null) {
            mRecyclerViewAdapter =
                AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                    Log.v(TAG_CLASS, "ItemClicked, member : ${it.second}")
                    mAddPeopleContentsBinding.viewModel?.selectPeople(it.second)
                    showMemberView()
                }

            mAddPeopleContentsBinding.recyclerView.apply {
                layoutManager =
                    GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                isFocusable = true
                descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                adapter = mRecyclerViewAdapter
            }

            setViewModel(mMemberViewModel)
        }
    }

    private fun addObserver() {
        Log.v(TAG_CLASS,"addObserver(...)")
        mAddPeopleContentsBinding.viewModel?.let {
            it.mNBBLiveData.observe(requireActivity(), Observer {
                Log.v(TAG_CLASS, "observer, call updateCircle(...) joinPeopleCount : ${it.mMemberCount}")
                val newMemberArrayList = arrayListOf<Member>()
                newMemberArrayList.addAll(it.mMemberList)
                mRecyclerViewAdapter.setItemList(newMemberArrayList)
            })

            it.mSelectJoinPeople.observe(requireActivity(), Observer {
                Log.v(TAG_CLASS, "Select JoinPeople : $it")
                it ?: return@Observer
                selectMember(it)
            })
        }

        mMemberViewModel.mMemberList.observe(requireActivity(), Observer {
            mAddPeopleContentsBinding.apply {
                if (it.isEmpty()) {
                    rowFavoriteMember.visibility = View.GONE
                    return@Observer
                } else {
                    rowFavoriteMember.visibility = View.VISIBLE
                }
                Log.i(TAG_CLASS,"KHS, update MemberList! $it")
                rowFavoriteMember.setTitle("LOCAL MEMBER")
                groupFavorite.visibility = View.VISIBLE
                rowFavoriteMember.initView(mPageViewModel, it)
                rowFavoriteMember.setControlScrolling()
//                rowFavoriteMember.setList(it)
            }
        })

        mMemberViewModel.gKakaoFriendList.observe(requireActivity(), Observer {
            mAddPeopleContentsBinding.apply {
                if (it.isEmpty()) {
                    rowFavoriteGroup.visibility = View.GONE
                    return@Observer
                } else {
                    rowFavoriteGroup.visibility = View.VISIBLE
                }

                rowFavoriteGroup.setTitle("KAKAO MEMBER")
                groupFavorite.visibility = View.VISIBLE
                rowFavoriteGroup.initView(mPageViewModel, it)
                rowFavoriteGroup.setControlScrolling()
//                rowFavoriteGroup.setList(it)
            }
        })
    }
}