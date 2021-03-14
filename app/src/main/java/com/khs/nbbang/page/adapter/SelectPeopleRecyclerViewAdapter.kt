package com.khs.nbbang.page.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.page.itemView.SelectMemberView
import com.khs.nbbang.user.Member

class SelectPeopleRecyclerViewAdapter (
    val mContext: Context,
    val mItemList: ArrayList<Pair<Member, Boolean>>,
    val mItemClick: (Pair<Int, Member>) -> Unit
) : RecyclerView.Adapter<SelectPeopleRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG

    var mAllItemList: ArrayList<Pair<Member, Boolean>> = arrayListOf()
    var mSelectedItemList: ArrayList<Member> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectMemberView(mContext)
        view.setViewSize(3)
        return ViewHolder(view, mItemClick)
    }

    override fun getItemCount(): Int {
        Log.v(TAG, "getItemCount : ${mItemList.size}")
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position), position)
    }

    inner class ViewHolder(itemView: SelectMemberView, itemClick: (Pair<Int, Member>) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: SelectMemberView = itemView
        var mItemClick = itemClick

        fun bind(pair: Pair<Member, Boolean>, position: Int) {
            mItemView.setCheckedMember(pair.second)
            mItemView.setMember(pair.first)
        }
    }

    fun setItem(item: Pair<Member, Boolean>) {
        Log.v(TAG,"setItem(...) member : ${item}")
        this.mItemList.add(mItemList.size, item)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: ArrayList<Pair<Member, Boolean>>) {
        Log.v(TAG,"setItem(...) memberList count : ${itemList.size}")
        mItemList.clear()
        mItemList.addAll(itemList)

        mAllItemList.clear()
        mAllItemList.addAll(itemList)
    }

    fun setSelectedMemberList(selectedItemList : ArrayList<Pair<Member,Boolean>>) {
        Log.v(TAG,"setSelectedMemberList(...) selectedItemList count : ${selectedItemList.size}")
        if (mAllItemList.isNullOrEmpty()) return
        for (allItem in mAllItemList) {
            for (selectedItem in selectedItemList) {
                if (allItem.first == selectedItem.first) {
                    mItemList.set(
                        mAllItemList.indexOf(allItem),
                        Pair(allItem.first, selectedItem.second)
                    )
                }
            }
        }
    }
}