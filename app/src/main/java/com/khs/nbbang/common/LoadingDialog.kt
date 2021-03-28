package com.khs.nbbang.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.utils.DisplayUtils

class LoadingDialog(context: Context) : Dialog(context) {
    init {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        val displaySize = DisplayUtils().getDisplaySize(context)
        val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(displaySize.x, displaySize.y)
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.setContentView(LoadingView(context), params)
    }

    inner class LoadingView(context: Context) : ConstraintLayout(context) {
        init {
            
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.cview_loading, this)
        }
    }
}
