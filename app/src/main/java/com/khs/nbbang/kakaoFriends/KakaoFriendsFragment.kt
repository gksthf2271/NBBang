package com.khs.nbbang.kakaoFriends

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.ActivityKakaoFriendsBinding
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class KakaoFriendsActivity : BaseActivity() {
    lateinit var mBinding : ActivityKakaoFriendsBinding
    private val gTabList : ArrayList<TabLayout.Tab> = arrayListOf()
    val gLoginViewModel by viewModel<LoginViewModel>()


    private val mPageViewList: MutableList<BaseFragment> = mutableListOf(
        FavoriteFriendsFragment(),
        AddFriendsFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kakao_friends)
        initData()
        initView()
        addObserver()
    }

    private fun addObserver() {
        gLoginViewModel.gIsLogin.observe(this, Observer {
            Log.v(TAG,"isLogin : $it")
        })
    }

    private fun initData() {
        gTabList.add(mBinding.tabLayout.newTab().apply {
            text = "즐겨찾기"
        })
        gTabList.add(mBinding.tabLayout.newTab().apply {
            text = "카카오 친구 추가하기"
        })
    }

    private fun initView() {
        mBinding.viewPager.apply {
            this.adapter = CustomViewPagerAdapter(
                supportFragmentManager,
                lifecycle,
                mPageViewList
            )

            TabLayoutMediator(mBinding.tabLayout, this) { tab, position ->

            }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}