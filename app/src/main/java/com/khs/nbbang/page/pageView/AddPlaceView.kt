package com.khs.nbbang.page.pageView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import kotlinx.android.synthetic.main.cview_add_place.view.*
import kotlinx.android.synthetic.main.cview_edit_place.view.*

class AddPlaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cview_add_place, this)
        initView()
    }

    fun initView() {
        btn_add.setOnClickListener {
            val rootView = findViewById<LinearLayout>(R.id.layout_group) as LinearLayout
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val infoView: ConstraintLayout =
                inflater.inflate(R.layout.cview_edit_place, rootView, false) as ConstraintLayout
            rootView.addView(infoView)
            infoView.txt_index.text = "${rootView.childCount} 차"
        }
    }
}