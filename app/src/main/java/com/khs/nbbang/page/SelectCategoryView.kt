package com.khs.nbbang.page

import android.util.Log
import com.khs.nbbang.base.BaseView

class SelectCategoryView : BaseView {
    val TAG = this.javaClass.name

    override fun showError(error: Int) {
        Log.v(TAG,"showError(...)")

    }
}