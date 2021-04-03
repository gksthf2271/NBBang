package com.khs.nbbang

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
        showMotion()
    }

    private fun showMotion() {
        mBinding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                Log.v(TAG,"onAnimationRepeat(...) : animation : $animation")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.v(TAG,"onAnimationEnd(...) : animation : $animation")
                gotoMain()
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.v(TAG,"onAnimationCancel(...) : animation : $animation")
                gotoMain()
            }

            override fun onAnimationStart(animation: Animator?) {
                Log.v(TAG,"onAnimationStart(...) : animation : $animation")
            }

        })
        mBinding.lottieAnimationView.playAnimation()
    }

    override fun onBackPressed() {
        Log.v(TAG,"blocked backKey")
    }

    private fun gotoMain() {
        launch<MainActivity>(startForResult)
    }
}