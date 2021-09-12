package com.khs.nbbang.kakaoFriends

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.databinding.CviewMemeberItemBinding
import com.khs.nbbang.databinding.CviewTextPeopleBinding
import com.khs.nbbang.page.itemView.SelectMemberView
import com.khs.nbbang.user.Member

class AddFriendsRecyclerViewAdapter(
    private var mRemoteMemberHashMap: HashMap<String, Member>,
    private var mLocalMemberHashMap: HashMap<String, Member>,
    private val mItemClick: (Member) -> Unit,
    private val viewUpdateCallback: (Boolean, Member) -> Unit
) : RecyclerView.Adapter<AddFriendsRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectMemberView(parent.context).mBinding
        (binding.root as SelectMemberView).setViewSize(3)
        return ViewHolder(binding, mItemClick)
    }

    override fun getItemCount(): Int {
        return mRemoteMemberHashMap.let { mRemoteMemberHashMap.values.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mRemoteMemberHashMap.values.toList().get(position))
    }

    inner class ViewHolder(val binding: CviewTextPeopleBinding, val itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(member: Member) {
            var itemView = binding.root as SelectMemberView
            with(itemView) {
                setCheckedMember(mLocalMemberHashMap.containsKey(member.kakaoId))
                setMember(member) { isSaveCallback, member ->
                    viewUpdateCallback(isSaveCallback, member)
                }
                setOnClickListener { mItemClick(member) }
            }
        }
    }

    fun setItem(memberHashMap: HashMap<String, Member>) {
        Log.v(TAG, "setItem(...) memberHashMap size : ${memberHashMap.values.size}")
        this.mRemoteMemberHashMap.clear()
        this.mRemoteMemberHashMap = memberHashMap
        notifyDataSetChanged()
    }

    fun findMemberByKakaoId(kakaoId: String): Member? {
        return mRemoteMemberHashMap.get(kakaoId)
    }
}