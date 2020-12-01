package com.khs.nbbang.page.pageView

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.People
import kotlinx.android.synthetic.main.cview_add_people.view.*
import kotlinx.android.synthetic.main.cview_edit_people.view.*


class AddPeopleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val TAG = this.javaClass.name

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cview_add_people, this)
        initView()
    }

    fun initView() {
        val gridViewAdapter = AddPeopleViewAdapter(context, mutableListOf())
        var dummyPeople = People(0," + ")
        gridViewAdapter.addItem(dummyPeople)
        view_grid.adapter = gridViewAdapter

        gridViewAdapter.addItem(People(1, "김한솔"))
        view_grid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Log.v(TAG, "TEST, onItemClicked, $position")
            if (position == 0) gridViewAdapter.addItem(
                gridViewAdapter.count,
                People(gridViewAdapter.count, "")
            )
        }
    }

    fun addDummyView(parent: ViewGroup?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var dummyView = inflater.inflate(R.layout.cview_edit_people, parent, false)

        dummyView!!.txt_name.inputType = InputType.TYPE_NULL
        dummyView!!.txt_name.setText(" + ")
        val itemSize = parent!!.childCount
        dummyView!!.layoutParams = ConstraintLayout.LayoutParams(itemSize, itemSize)
        parent!!.addView(dummyView)
    }

}