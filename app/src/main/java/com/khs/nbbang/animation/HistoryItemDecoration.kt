package com.khs.nbbang.animation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HistoryItemDecoration(divHeight: Int) : RecyclerView.ItemDecoration() {
    var mDivHeight:Int
    init{
        mDivHeight = divHeight
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mDivHeight
        outRect.left = mDivHeight
        outRect.right = mDivHeight
    }

}