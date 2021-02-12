package com.khs.nbbang.page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.databinding.ActivityFreeUserBinding
import com.khs.nbbang.page.viewModel.PageViewModel
import org.koin.android.viewmodel.ext.android.viewModel

//class FreeUserActivity : PageActivity(){
//    lateinit var mBinding: ActivityFreeUserBinding
//    val mViewModel by viewModel<PageViewModel>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_free_user)
//        mBinding.viewModel = mViewModel
//        initView()
//    }
//
//    fun initView() {
//        val text = when (intent.getIntExtra("input", 0)) {  // 받아온 데이터로 처리
//            100 -> "성공"
//            else -> "없음"
//        }
//        setResult(Activity.RESULT_OK, Intent().apply { putExtra("result", text) })
//
//        mBinding.viewPager.adapter = getPageAdapter()
//        mBinding.viewPager.currentItem = 0
//        mBinding.viewPager.setPagingEnable(false)
//        mBinding.viewPager.offscreenPageLimit = mPageViewList.size - 2
//        mBinding.viewIndicator.setViewPager(mBinding.viewPager)
//    }
//}