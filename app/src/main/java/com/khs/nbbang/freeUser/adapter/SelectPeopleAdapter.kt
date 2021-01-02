package com.khs.nbbang.freeUser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.utils.DisplayUtils
import kotlinx.android.synthetic.main.cview_text_people.view.*

class SelectPeopleAdapter (context: Context, itemList: MutableList<People>, checkedChangeListener: CompoundButton.OnCheckedChangeListener) : BaseAdapter() {
    val TAG = this.javaClass.name
    var mItemList: MutableList<People>
    var mItemView : MutableList<View>
    var mContext: Context
    var mCheckedChangeListener : CompoundButton.OnCheckedChangeListener

    init {
        mItemList = itemList
        mContext = context
        mItemView = mutableListOf()
        mCheckedChangeListener = checkedChangeListener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (mItemView.size <= position) {
            mItemView.add(position, addPeopleView(parent, position))
        }
        return mItemView.get(position)
    }

    private fun addPeopleView(parent: ViewGroup?, position: Int) : View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: ConstraintLayout =
            inflater.inflate(R.layout.cview_text_people, parent, false) as ConstraintLayout
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        itemView.checkbox_name.text = mItemList.get(position).mName
        itemView.checkbox_name.setOnCheckedChangeListener(mCheckedChangeListener)

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