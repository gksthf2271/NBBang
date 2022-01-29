package com.khs.nbbang.kakaoFriends

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.khs.nbbang.animation.ZoomOutPageTransformer
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentKakaoFriendsBinding
import com.khs.nbbang.localMember.GroupManagementFragment
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class KakaoFriendsFragment : BaseFragment() {
    lateinit var mBinding : FragmentKakaoFriendsBinding
//    val gLoginViewModel by viewModel<LoginViewModel>()
    private val gLoginViewModel by sharedViewModel<LoginViewModel>()
    private val gMemberManagementViewModel by viewModel<MemberManagementViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentKakaoFriendsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.v(TAG_CLASS,"keyCode: $keyCode , event : ${event}")
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = gMemberManagementViewModel
        initView()
        addObserver()
    }

    private fun addObserver() {
        gLoginViewModel.gIsLogin.observe(requireActivity(), Observer {
            Log.v(TAG_CLASS,"isLogin : $it")
        })
    }

    private fun initView() {
        val defaultPageViewList: MutableList<BaseFragment> = mutableListOf(GroupManagementFragment())
        mBinding.viewPager.apply {
            defaultPageViewList.run{
                if (gLoginViewModel.checkKakaoLoginByLocalValue()){
                    this.add(KakaoFavoriteFriendsFragment())
                }
            }
            this.adapter = CustomViewPagerAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                defaultPageViewList
            )

            if (defaultPageViewList.size > 1) {
                mBinding.viewPager.setPageTransformer(ZoomOutPageTransformer())
                mBinding.viewIndicator.setViewPager2(mBinding.viewPager)
                mBinding.viewIndicator.visibility = View.VISIBLE
            } else {
                mBinding.viewIndicator.visibility = View.INVISIBLE
            }

//            TabLayoutMediator(mBinding.tabLayout, this) { tab, position ->
//                when(position) {
//                    0 -> {
//                        tab.text = "로컬 멤버"
//                    }
//                    1 -> {
//                        tab.text = "카카오 멤버"
//                    }
//                }
//            }.attach()
        }
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG_CLASS,"makeCustomLoadingView(...)")
        return null
    }
}