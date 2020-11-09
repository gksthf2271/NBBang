package com.khs.nbbang.page.customView

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import kotlinx.android.synthetic.main.cview_select_count.view.*

class PeopleCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cview_select_count, this)
        initView()
    }

    fun initView() {
        btn_minus.setOnClickListener {
            if (TextUtils.isEmpty(edit_count.text)) {
                edit_count.setText("0")
            } else if (Integer.parseInt(edit_count.text.toString()) <= 0) {
                edit_count.setText("0")
            } else {
                var currentNum: Int = Integer.parseInt(edit_count.text.toString())
                currentNum--
                edit_count.setText(currentNum.toString())
            }
        }
        btn_plus.setOnClickListener {
            if (TextUtils.isEmpty(edit_count.text)) {
                edit_count.setText("1")
            } else {
                var currentNum: Int = Integer.parseInt(edit_count.text.toString())
                currentNum++
                edit_count.setText(currentNum.toString())
            }
        }
    }
}