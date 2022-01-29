package com.khs.nbbang.page.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.page.itemView.SelectMemberView
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.LogUtil

class SelectPeopleRecyclerViewAdapter (
    val mContext: Context,
    val mItemList: ArrayList<Pair<Member, Boolean>>,
    val gViewUpdateCallback : (Boolean, Member) -> Unit
) : RecyclerView.Adapter<SelectPeopleRecyclerViewAdapter.ViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName
    private val LOG_TAG: String = LogUtil.TAG_UI
    val DEBUG = BuildConfig.DEBUG

    var mAllItemList: ArrayList<Pair<Member, Boolean>> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectMemberView(mContext)
        view.setViewSize(3)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mItemList.run { mItemList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mItemList.get(position), position)
    }

    inner class ViewHolder(itemView: SelectMemberView) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: SelectMemberView = itemView

        fun bind(pair: Pair<Member, Boolean>, position: Int) {
            mItemView.setCheckedMember(pair.second)
            mItemView.setMember(pair.first) { isSaveCallback, member ->
                gViewUpdateCallback(isSaveCallback, member)
            }
        }
    }

    fun setItem(item: Pair<Member, Boolean>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) member : ${item}")
        this.mItemList.add(mItemList.size, item)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: ArrayList<Pair<Member, Boolean>>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) memberList count : ${itemList.size}")
        mItemList.clear()
        mItemList.addAll(itemList)

        mAllItemList.clear()
        mAllItemList.addAll(itemList)
    }

    fun setSelectedMemberList(selectedItemList : ArrayList<Pair<Member,Boolean>>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setSelectedMemberList(...) selectedItemList count : ${selectedItemList.size}")
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