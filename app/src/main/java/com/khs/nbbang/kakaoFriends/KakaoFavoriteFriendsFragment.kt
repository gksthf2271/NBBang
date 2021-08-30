package com.khs.nbbang.kakaoFriends

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.databinding.FragmentKakaoFavoriteFriendsBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.localMember.MemberRecyclerViewAdapter
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import kotlinx.android.synthetic.main.cview_page_title.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class KakaoFavoriteFriendsFragment : BaseFragment() {
    lateinit var mBinding : FragmentKakaoFavoriteFriendsBinding
    val gLoginViewModel: LoginViewModel by sharedViewModel()
    val gMemberViewModel: MemberManagementViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kakao_favorite_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = gMemberViewModel
        initView()
        addObserver()
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    private fun initView() {
        if (mBinding.recyclerFriendList.adapter == null) {
            mBinding.recyclerFriendList.apply {
                layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                adapter =
                    AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                        Log.v(TAG, "ItemClicked : $it")
//                        mBinding.viewModel!!.selectMember(it)
                    }
            }
            addObserver()
        }
        mBinding.viewModel.let {
            mBinding.groupTitle.txt_title.text =
                "Kakao Favorite Member"
            it!!.showFavoriteMemberListByType(MemberType.TYPE_KAKAO)
        }

        mBinding.btnAdd.setOnClickListener {
            showAddKakaoFriendsDialog()
        }
    }

    private fun addObserver() {
        mBinding.viewModel ?: return

        mBinding.viewModel!!.mShowLoadingView.observe(requireActivity(), Observer {
            when (it) {
                true -> showLoadingView()
                false -> hideLoadingView()
            }
        })

        mBinding.viewModel!!.gKakaoFriendList.observe(requireActivity(), Observer {
            val adapter = (mBinding.recyclerFriendList.adapter as? AddPeopleRecyclerViewAdapter)
                ?: return@Observer
            adapter.setItemList(ArrayList(it))
            mBinding.groupTitle.txt_sub_title.text =
                "${it.size}명 대기중..."
            mBinding.viewModel!!.updateLoadingFlag(false)
        })

        mBinding.viewModel!!.mSelectMember.observe(requireActivity(), Observer {
            Log.v(TAG, "Select Member : $it")
            it ?: return@Observer
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.v(TAG,"onKeyDown(...) : keyCode : $keyCode, KeyEvent : ${event}")
        return false
    }

    private fun showAddKakaoFriendsDialog() {
        Log.v(TAG, "showAddKakaoFriendsDialog(...)")
        val addFriendsFragment = AddFriendsDialogFragment.getInstance()
        when {
            addFriendsFragment.isAdded -> {
                return
            }
            else -> {
                addFriendsFragment.show(requireActivity().supportFragmentManager, tag)
            }
        }
    }
}