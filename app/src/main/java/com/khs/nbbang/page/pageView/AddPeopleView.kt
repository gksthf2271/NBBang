package com.khs.nbbang.page.pageView

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.GridLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import kotlinx.android.synthetic.main.cview_edit_people.view.*


class AddPeopleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cview_add_people, this)
        initView()
    }

    //TODO : GridLayout -> GridView로 변경 해야됨

    fun initView() {
        val rootView = findViewById<GridLayout>(R.id.layout_group) as GridLayout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val addView: ConstraintLayout =
            inflater.inflate(R.layout.cview_edit_people, rootView, false) as ConstraintLayout

        addView.txt_name.inputType = InputType.TYPE_NULL
        addView.txt_name.setText(" + ")
        val itemSize = getItemViewSize()
        addView.layoutParams = LayoutParams(itemSize, itemSize)
        rootView.addView(addView)

        addView.txt_name.setOnClickListener {
            val peopleView: ConstraintLayout =
                inflater.inflate(R.layout.cview_edit_people, rootView, false) as ConstraintLayout
            peopleView.layoutParams = LayoutParams(itemSize, itemSize)
            rootView.addView(peopleView, 0)
        }
    }

    fun getItemViewSize(): Int {
        val metrics = resources.displayMetrics
        val screenWidth = metrics.widthPixels
        return ((screenWidth * 0.95) / 3).toInt()
    }
}