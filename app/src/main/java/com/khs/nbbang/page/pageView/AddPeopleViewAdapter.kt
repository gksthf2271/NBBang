package com.khs.nbbang.page.pageView

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.utils.DisplayUtils
import kotlinx.android.synthetic.main.cview_edit_people.view.*

class AddPeopleViewAdapter(context: Context, itemList: MutableList<People>) : BaseAdapter() {
    val TAG = this.javaClass.name
    var mItemList: MutableList<People>
    var mContext: Context

    init {
        mItemList = itemList
        mContext = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (position == 0) {
             return addPlusView(parent)
        } else {
            return addPeopleView(parent, position)
        }
    }

    private fun addPlusView(parent: ViewGroup?) : View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: ConstraintLayout =
            inflater.inflate(R.layout.cview_edit_people_plus, parent, false) as ConstraintLayout
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)

        return itemView
    }

    private fun addPeopleView(parent: ViewGroup?, position: Int) : View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: ConstraintLayout =
            inflater.inflate(R.layout.cview_edit_people, parent, false) as ConstraintLayout
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        itemView.txt_name.setText(mItemList.get(position).mName)
        return itemView
    }

    override fun getItem(position: Int): Any {
        return mItemList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return mItemList.get(position).mIndex.toLong()
    }

    override fun getCount(): Int {
        return mItemList.size
    }

    fun addItem(people: People) {
        mItemList.add(people)
    }

    fun addItem(index: Int, people: People) {
        mItemList.add(index, people)
        notifyDataSetChanged()
    }
}