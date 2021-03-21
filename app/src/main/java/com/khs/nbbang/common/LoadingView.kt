package com.khs.nbbang.common

import android.app.Dialog
import android.content.Context
import com.khs.nbbang.R

class LoadingView(context: Context) : Dialog(context) {
    init {
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.cview_loading)
    }
}