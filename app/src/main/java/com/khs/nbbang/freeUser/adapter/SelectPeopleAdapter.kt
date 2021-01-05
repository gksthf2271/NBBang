package com.khs.nbbang.freeUser.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.ItemObj.PeopleListObj
import com.khs.nbbang.utils.DisplayUtils
import kotlinx.android.synthetic.main.cview_text_people.view.*

class SelectPeopleAdapter (context: Context, itemList: MutableList<People>) : BaseAdapter() {
    val TAG = this.javaClass.name
    var mItemList: MutableList<People>
    var mItemView : MutableList<View>
    var mContext: Context
    var mSelectPeopleListObj : PeopleListObj?

    init {
        mItemList = itemList
        mContext = context
        mItemView = mutableListOf()
        mSelectPeopleListObj = null
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
        var people = mItemList.get(position)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        itemView.tag = people
        itemView.checkbox_name.text = people.mName

        mSelectPeopleListObj?.let {
            for (obj in it.mPeopleList){
                if (people == obj) {
                    Log.v(TAG,"Select People, ${people.mName}")
                    itemView.checkbox_name.isChecked = true
                }
            }
        }
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

    fun getSelectedPeopleList() : MutableList<People>{
        var checkedPeopleList = mutableListOf<People>()
        for (index in 0 .. mItemView.size - 1) {
            if (mItemView.get(index).tag is People && mItemView.get(index).checkbox_name.isChecked) {
                checkedPeopleList.add(mItemView.get(index).tag as People)
            }
        }
        return checkedPeopleList
    }

    fun setSelectPeople(peopleList: PeopleListObj) {
        Log.v(TAG,"selectPeople, $peopleList")
        mSelectPeopleListObj = peopleList
    }
}