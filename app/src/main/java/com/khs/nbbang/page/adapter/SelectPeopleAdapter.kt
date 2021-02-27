package com.khs.nbbang.page.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.JoinPeople
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.utils.DisplayUtils
import kotlinx.android.synthetic.main.cview_text_people.view.*

class SelectPeopleAdapter (context: Context, itemList: MutableList<JoinPeople>) : BaseAdapter() {
    val TAG = this.javaClass.name
    var mItemList: MutableList<JoinPeople>
    var mItemView : MutableList<View>
    var mContext: Context
    var mSelectNBB : NBB?

    init {
        mItemList = itemList
        mContext = context
        mItemView = mutableListOf()
        mSelectNBB = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (mItemView.size <= position) {
            mItemView.add(position, addJoinPeopleView(parent, position))
        }
        return mItemView.get(position)
    }

    private fun addJoinPeopleView(parent: ViewGroup?, position: Int) : View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: ConstraintLayout =
            inflater.inflate(R.layout.cview_text_people, parent, false) as ConstraintLayout
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)
        var joinPeople = mItemList.get(position)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        itemView.tag = joinPeople
        itemView.checkbox_name.text = joinPeople.name

        mSelectNBB?.let {
            for (obj in it.mJoinPeopleList){
                if (joinPeople == obj) {
                    Log.v(TAG,"Select joinPeople, ${joinPeople.name}")
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
        return mItemList.get(position).index.toLong()
    }

    override fun getCount(): Int {
        return mItemList.size
    }

    fun addItem(joinPeople: JoinPeople) {
        mItemList.add(joinPeople)
    }

    fun addItem(index: Int, joinPeople: JoinPeople) {
        mItemList.add(index, joinPeople)
        notifyDataSetChanged()
    }

    fun getSelectedJoinPeopleList() : MutableList<JoinPeople>{
        var checkedJoinPeopleList = mutableListOf<JoinPeople>()
        for (index in 0 until mItemView.size) {
            if (mItemView.get(index).tag is JoinPeople && mItemView.get(index).checkbox_name.isChecked) {
                checkedJoinPeopleList.add(mItemView.get(index).tag as JoinPeople)
            }
        }
        return checkedJoinPeopleList
    }

    fun setSelectJoinPeople(nbb: NBB) {
        Log.v(TAG,"setSelectJoinPeople, $nbb")
        mSelectNBB = nbb
    }
}