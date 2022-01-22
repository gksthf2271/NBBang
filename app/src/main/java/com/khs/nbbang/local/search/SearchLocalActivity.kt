package com.khs.nbbang.local.search

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivitySearchBinding
import com.khs.nbbang.search.KakaoLocalViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SearchLocalActivity : BaseActivity() {
    lateinit var mBinding: ActivitySearchBinding
    private val mSearchViewModel by viewModel<KakaoLocalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        mBinding.viewModel = mSearchViewModel
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