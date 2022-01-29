package com.khs.nbbang.localMember

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.databinding.CviewMemeberItemBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.LogUtil


class MemberRecyclerViewAdapter(
    private val mMemberList: ArrayList<Member>,
    private val itemClick: (Member) -> Unit
) :
    RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName
    private val LOG_TAG: String = LogUtil.TAG_UI
    val DEBUG = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CviewMemeberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int {
        return mMemberList.run { mMemberList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mMemberList.get(position))
    }

    class ViewHolder(val binding: CviewMemeberItemBinding, val itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(member: Member) {
            binding.txtName.text = member.name
            binding.txtDescription.text = member.description
            GlideUtils().drawMemberProfile(binding.imgProfile, member, null)
            binding.root.setOnClickListener { itemClick(member) }
        }
    }

    fun setItem(members: List<Member>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) inputMembers size : ${members.size}")
        this.mMemberList.clear()
        this.mMemberList.addAll(members)
        notifyDataSetChanged()
    }
}