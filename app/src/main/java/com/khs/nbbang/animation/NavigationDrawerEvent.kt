package com.khs.nbbang.animation

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class NavigationDrawerEvent(private val contentView : View, private val navigationView: NavigationView) : DrawerLayout.SimpleDrawerListener() {

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        super.onDrawerSlide(drawerView, slideOffset)
        contentView.setX(navigationView.width * slideOffset)
        val lp = contentView.layoutParams as ViewGroup.MarginLayoutParams

        lp.height = (drawerView.height - (drawerView.height * slideOffset * 0.3f)).toInt()
        lp.topMargin = (drawerView.height - lp.height) / 2
        contentView.layoutParams = lp
    }

    override fun onDrawerClosed(drawerView: View) {
        super.onDrawerClosed(drawerView)
    }
}