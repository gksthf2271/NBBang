package com.khs.nbbang.kakaoFriends

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.R
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_memeber_item.view.*

class AddFriendsRecyclerViewAdapter (
    private val mMemberList: ArrayList<Member>,
    private val itemClick: (Member) -> Unit
) : RecyclerView.Adapter<AddFriendsRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cview_selectable_member_item, parent, false), itemClick
        )
    }

    override fun getItemCount(): Int {
        if(DEBUG) Log.v(TAG,"getItemCount : ${mMemberList.size}")
        return mMemberList.let { mMemberList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mMemberList.get(position))
    }

    class ViewHolder(itemView: View, itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(member: Member) {
            mItemView.txt_name.text = member.name
            GlideUtils().drawMemberProfile(mItemView.img_profile, member, null)
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