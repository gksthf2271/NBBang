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
import com.google.android.material.tabs.TabLayoutMediator
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentKakaoFriendsBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class KakaoFriendsFragment : BaseFragment() {
    lateinit var mBinding : FragmentKakaoFriendsBinding
    val gLoginViewModel by viewModel<LoginViewModel>()
    val gMemberManagementViewModel by viewModel<MemberManagementViewModel>()


    private val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        FavoriteFriendsFragment(),
        AddFriendsFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kakao_friends, container, false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.v(TAG,"keyCode: $keyCode , event : ${event}")
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = gMemberManagementViewModel
        initData()
        initView()
        addObserver()
    }

    private fun addObserver() {
        gLoginViewModel.gIsLogin.observe(requireActivity(), Observer {
            Log.v(TAG,"isLogin : $it")
        })
    }

    private fun initData() {
    }

    private fun initView() {
        mBinding.viewPager.apply {
            this.adapter = CustomViewPagerAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                mPageViewList
            )

            TabLayoutMediator(mBinding.tabLayout, this) { tab, position ->
                when(position) {
                    0 -> {
                        tab.text = "즐겨찾기"
                    }
                    1 -> {
                        tab.text = "카카오 친구 추가하기"
                    }
                }
            }.attach()
        }
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }
}