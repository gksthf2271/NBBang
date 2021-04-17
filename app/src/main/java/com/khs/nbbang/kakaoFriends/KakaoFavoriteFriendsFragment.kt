package com.khs.nbbang.kakaoFriends

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentKakaoFavoriteFriendsBinding
import com.khs.nbbang.databinding.FragmentKakaoFriendsBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.group.MemberRecyclerViewAdapter
import com.khs.nbbang.login.LoginViewModel
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
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(HistoryItemDecoration(30))
                adapter =
                    MemberRecyclerViewAdapter(arrayListOf()) {
                        Log.v(TAG, "ItemClicked : $it")
//                        mBinding.viewModel!!.selectMember(it)
                    }
            }
            addObserver()
        }
    }

    private fun addObserver() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.v(TAG,"onKeyDown(...) : keyCode : $keyCode, KeyEvent : ${event}")
        return false
    }
}