package com.khs.nbbang.search

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.ActivitySearchBinding
import com.khs.nbbang.utils.FragmentUtils
import com.khs.nbbang.utils.LogUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchLocalActivity : BaseActivity() {
    lateinit var mBinding: ActivitySearchBinding
    private val mSearchViewModel: KakaoLocalViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        mBinding.viewModel = mSearchViewModel
    }

    override fun onStart() {
        super.onStart()
        FragmentUtils.loadFragment(
            SearchFragment(),
            mBinding.fragmentContainer.id,
            supportFragmentManager
        )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogUtil.dLog(LOG_TAG, TAG_CLASS, "onKeyDown(...) event : $event, keyCode : $keyCode")
        if (supportFragmentManager.fragments.isEmpty())
            return false

        val currentFragment = supportFragmentManager.fragments[0]
        val fragment = currentFragment as? BaseFragment

        if (fragment != null && fragment.onKeyDown(keyCode, event))
            return true

        return super.onKeyDown(keyCode, event)
    }
}