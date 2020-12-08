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
        mViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
        initView()
    }

    fun initView() {
        mViewModel.let {
            mBinding.viewPager.adapter = it.mViewPagerAdapter
            mBinding.viewPager.currentItem = 0
            mBinding.viewPager.setPagingEnable(false)
            mBinding.viewIndicator.setViewPager(mBinding.viewPager)
            it.mPeopleCount.observe(this, Observer {
                Log.v(TAG, "people count : ${it}")
                mViewModel.updatePeopleCircle()
            })
        }
    }
}