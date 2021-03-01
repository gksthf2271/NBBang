package com.khs.nbbang.animation

import android.view.View
import android.widget.RelativeLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class NavigationDrawerEvent(private val contentView : RelativeLayout, private val navigationView: NavigationView) : DrawerLayout.SimpleDrawerListener() {
    private val MIN_SCALE = 0.85f
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        val diffScaledOffset: Float = slideOffset * (1 - MIN_SCALE)
        val offsetScale = 1 - diffScaledOffset
        contentView.scaleX = offsetScale
        contentView.scaleY = offsetScale

        val xOffset = drawerView.width * slideOffset
        val xOffsetDiff = contentView.width * diffScaledOffset / 2
        val xTranslation = xOffset - xOffsetDiff
        contentView.translationX = xTranslation
    }

    override fun onDrawerClosed(drawerView: View) {
        super.onDrawerClosed(drawerView)
    }
}