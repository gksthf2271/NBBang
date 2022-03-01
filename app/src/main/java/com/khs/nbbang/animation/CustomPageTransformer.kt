package com.khs.nbbang.animation

import android.animation.ArgbEvaluator
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.khs.nbbang.page.pager.CustomViewPagerAdapter


class CustomPageTransformer(private val mViewPager: ViewPager2, private val mTargetView: View? = null, private val isAccessEvent: Boolean = false) : ViewPager2.PageTransformer, ViewPager2.OnPageChangeCallback() {
    private val argbEvaluator: ArgbEvaluator
    private var color: Int = -1

    init {
        mViewPager.registerOnPageChangeCallback(this)
        argbEvaluator = ArgbEvaluator()
    }

    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mTargetView ?: return
        color = argbEvaluator.evaluate(
            positionOffset,
            (mViewPager.adapter as CustomViewPagerAdapter).getFragmentPointColor(position),
            (mViewPager.adapter as CustomViewPagerAdapter).getFragmentPointColor(position + 1)
        ) as Int
        mTargetView.setBackgroundColor(color)
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}