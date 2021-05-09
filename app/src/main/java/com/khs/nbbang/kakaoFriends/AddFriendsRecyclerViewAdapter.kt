package com.khs.nbbang.kakaoFriends

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.page.adapter.SelectPeopleRecyclerViewAdapter
import com.khs.nbbang.page.itemView.SelectMemberView
import com.khs.nbbang.user.Member

class AddFriendsRecyclerViewAdapter (
    private val mMemberList: ArrayList<Member>,
    private val itemClick: (Member) -> Unit
) : RecyclerView.Adapter<AddFriendsRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectMemberView(parent.context)
        view.setViewSize(3)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        if(DEBUG) Log.v(TAG,"getItemCount : ${mMemberList.size}")
        return mMemberList.let { mMemberList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mMemberList.get(position))
    }

    class ViewHolder(itemView: SelectMemberView, itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: SelectMemberView = itemView
        var mItemClick = itemClick

        fun bind(member: Member) {
            mItemView.setMember(member, false)
            mItemView.setOnClickListener { mItemClick(member) }
        }
    }

    fun setItem(members: List<Member>) {
        Log.v(TAG,"setItem(...) inputMembers size : ${members.size}")
        this.mMemberList.clear()
        this.mMemberList.addAll(members)
        notifyDataSetChanged()
    }
}