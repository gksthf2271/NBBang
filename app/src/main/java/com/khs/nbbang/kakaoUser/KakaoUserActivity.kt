package com.khs.nbbang.kakaoUser

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.PageActivity
import com.khs.nbbang.databinding.ActivityFreeUserBinding
import com.khs.nbbang.freeUser.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class KakaoUserActivity : PageActivity() {
    lateinit var mBinding: ActivityFreeUserBinding
    val mViewModel by viewModel<PageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_free_user)
        initView()
    }

    fun initView() {
        mBinding.viewPager.adapter = getPageAdapter()
        mBinding.viewPager.currentItem = 0
        mBinding.viewPager.setPagingEnable(false)
        mBinding.viewPager.offscreenPageLimit = mPageViewList.size - 2
        mBinding.viewIndicator.setViewPager(mBinding.viewPager)
    }
}