package com.khs.nbbang

import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityLoadingBinding

class LoadingActivity : BaseActivity() {
    lateinit var mBinding : ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_loading)
        initView()
    }

    private fun initView() {
        var handler = Handler()
        handler.postDelayed({
            gotoMain()
        }, 1300)
        showMotion()
    }

    private fun showMotion() {
        mBinding.motionLayout.setTransition(R.id.motion_loading)
        mBinding.motionLayout.performClick()
    }

    private fun gotoMain() {
        launch<MainActivity>(startForResult)
    }
}