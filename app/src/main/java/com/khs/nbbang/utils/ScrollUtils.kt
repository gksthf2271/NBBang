package com.khs.nbbang.utils

import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.viewpager2.widget.ViewPager2


object ScrollUtils {
    fun controlHorizontalScrollingInViewPager2(
        recyclerView: RecyclerView,
        viewPager2: ViewPager2
    ) {
        val onTouchListener: OnItemTouchListener = object : OnItemTouchListener {
            var lastX = 0
            override fun onInterceptTouchEvent(
                rv: RecyclerView,
                e: MotionEvent
            ): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> lastX = e.x.toInt()
                    MotionEvent.ACTION_MOVE -> {
                        val isScrollingRight = e.x < lastX
                        viewPager2.isUserInputEnabled =
                            isScrollingRight && (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1 || !isScrollingRight && (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition() == 0
                    }
                    MotionEvent.ACTION_UP -> {
                        lastX = 0
                        viewPager2.isUserInputEnabled = true
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }
        recyclerView.addOnItemTouchListener(onTouchListener)
    }
}