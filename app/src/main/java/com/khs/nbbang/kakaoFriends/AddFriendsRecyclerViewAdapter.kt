package com.khs.nbbang.kakaoFriends

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.page.itemView.SelectMemberView
import com.khs.nbbang.user.Member

class AddFriendsRecyclerViewAdapter (
    private var mRemoteMemberHashMap: HashMap<String, Member>,
    private var mLocalMemberHashMap: HashMap<String, Member>,
    private val itemClick: (Member) -> Unit,
    private val viewUpdateCallback : (Boolean, Member) -> Unit
) : RecyclerView.Adapter<AddFriendsRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SelectMemberView(parent.context)
        view.setViewSize(3)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return mRemoteMemberHashMap.let { mRemoteMemberHashMap.values.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mRemoteMemberHashMap.values.toList().get(position))
    }

    inner class ViewHolder(itemView: SelectMemberView, itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: SelectMemberView = itemView
        var mItemClick = itemClick

        fun bind(member: Member) {
            mItemView.setCheckedMember(mLocalMemberHashMap.containsKey(member.kakaoId))
            mItemView.setMember(member) { isSaveCallback, member ->
                viewUpdateCallback(isSaveCallback, member)
            }
            mItemView.setOnClickListener { mItemClick(member) }
        }
    }

    fun setItem(memberHashMap: HashMap<String, Member>) {
        Log.v(TAG,"setItem(...) memberHashMap size : ${memberHashMap.values.size}")
        this.mRemoteMemberHashMap.clear()
        this.mRemoteMemberHashMap = memberHashMap
        notifyDataSetChanged()
    }

    fun findMemberByKakaoId(kakaoId: String) : Member? {
        return mRemoteMemberHashMap.get(kakaoId)
    }
}