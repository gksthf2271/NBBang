package com.khs.nbbang.freeUser

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityFreeUserBinding
import com.khs.nbbang.freeUser.viewModel.PageViewModel

class FreeUserActivity : BaseActivity() {
    lateinit var mBinding: ActivityFreeUserBinding
    lateinit var mViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView(this@FreeUserActivity, R.layout.activity_free_user)
        mViewModel = ViewModelProvider(this@FreeUserActivity, PageViewModel.PageViewModelFactory(supportFragmentManager, application)).get(PageViewModel::class.java)
        initView()
    }

    fun initView() {
        mBinding.viewPager.adapter = mViewModel._viewPagerAdapter.value
        mBinding.viewPager.currentItem = 0
        mBinding.viewPager.setPagingEnable(false)
        mBinding.viewPager.offscreenPageLimit = mViewModel.mPageViewList.size - 2
        mBinding.viewIndicator.setViewPager(mBinding.viewPager)

        mViewModel._counter.observe(this, Observer {
            mViewModel.updatePeopleCircle()
        })
    }
}