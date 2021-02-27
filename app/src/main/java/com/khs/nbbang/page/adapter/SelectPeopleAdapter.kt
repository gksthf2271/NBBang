package com.khs.nbbang.page.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DisplayUtils
import kotlinx.android.synthetic.main.cview_text_people.view.*

class SelectPeopleAdapter (context: Context, itemList: MutableList<Member>) : BaseAdapter() {
    val TAG = this.javaClass.name
    var mItemList: MutableList<Member>
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
        var member = mItemList.get(position)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        itemView.tag = member
        itemView.checkbox_name.text = member.name

        mSelectNBB?.let {
            for (obj in it.mMemberList){
                if (member == obj) {
                    Log.v(TAG,"Select member, ${member.name}")
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

    fun addItem(member: Member) {
        mItemList.add(member)
    }

    fun addItem(index: Int, member: Member) {
        mItemList.add(index, member)
        notifyDataSetChanged()
    }

    fun getSelectedMemberList() : MutableList<Member>{
        var checkedMemberList = mutableListOf<Member>()
        for (index in 0 until mItemView.size) {
            if (mItemView.get(index).tag is Member && mItemView.get(index).checkbox_name.isChecked) {
                checkedMemberList.add(mItemView.get(index).tag as Member)
            }
        }
        return checkedMemberList
    }

    fun setSelectMember(nbb: NBB) {
        Log.v(TAG,"setSelectMember, $nbb")
        mSelectNBB = nbb
    }
}