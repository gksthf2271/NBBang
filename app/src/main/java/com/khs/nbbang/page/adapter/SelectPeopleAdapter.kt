package com.khs.nbbang.page.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.itemView.SelectPeopleView
import com.khs.nbbang.user.Member

class SelectPeopleAdapter (private val mContext: Context, private val mItemList: MutableList<Member>) : BaseAdapter() {
    val TAG = this.javaClass.name
    private val mItemViewList : MutableList<View> = mutableListOf()
    var mSelectNBB : NBB?

    init {
        mSelectNBB = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        Log.v(TAG,"TEST, getView(...)")

        var itemView : View? = convertView

        if (convertView == null) {
            itemView = addJoinPeopleView(position)
            mItemViewList.add(position, itemView)
        }

        return itemView!!
    }

    private fun addJoinPeopleView(position: Int) : View {
        Log.v(TAG,"addJoinPeopleView(...) position :$position")
        var itemView = SelectPeopleView(mContext)
        itemView.setViewSize(3)
        var member = mItemList.get(position)

        itemView.setMember(member.apply {
            mSelectNBB?.let {
                Log.v(TAG,"currentMember : ${member.name}")
                itemView.setCheckedMember(it.mMemberList.contains(member))
            }
        })

        return itemView
    }

    override fun getItem(position: Int): Any {
        Log.v(TAG,"getItem(...) position : $position")
        return mItemList.get(position)
    }

    override fun getItemId(position: Int): Long {
        Log.v(TAG,"getItemId(...) position : $position")
        return mItemList.get(position).id
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
        for (itemView in mItemViewList) {
            if (itemView.tag is Member && (itemView as SelectPeopleView).isCheckedMember()) {
                checkedMemberList.add(itemView.tag as Member)
            }
        }
        return checkedMemberList
    }

    fun setSelectMember(nbb: NBB) {
        Log.v(TAG,"setSelectMember, $nbb")
        mSelectNBB = nbb
    }
}