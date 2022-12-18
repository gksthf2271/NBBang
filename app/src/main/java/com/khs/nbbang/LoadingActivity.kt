package com.khs.nbbang

import android.animation.Animator
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityLoadingBinding
import com.khs.nbbang.utils.LogUtil

class LoadingActivity : BaseActivity() {
    lateinit var mBinding : ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_loading)
        initView()
    }

    private fun initView() {
        showMotion()
    }

    private fun showMotion() {
        mBinding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onAnimationRepeat(...) : animation : $animation")
            }

            override fun onAnimationEnd(animation: Animator) {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onAnimationEnd(...) : animation : $animation")
                gotoMain()
            }

            override fun onAnimationCancel(animation: Animator) {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onAnimationCancel(...) : animation : $animation")
                gotoMain()
            }

            override fun onAnimationStart(animation: Animator) {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onAnimationStart(...) : animation : $animation")
            }

        })
        mBinding.lottieAnimationView.playAnimation()
    }

    override fun onBackPressed() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "blocked backKey")
    }

    private fun gotoMain() {
        launch<MainActivity>(startForResult)
    }
}