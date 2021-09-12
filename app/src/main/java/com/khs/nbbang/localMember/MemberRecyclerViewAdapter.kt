package com.khs.nbbang.localMember

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.R
import com.khs.nbbang.databinding.CviewMemeberItemBinding
import com.khs.nbbang.databinding.CviewShareResultItemBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_memeber_item.view.*


class MemberRecyclerViewAdapter(
    private val mMemberList: ArrayList<Member>,
    private val itemClick: (Member) -> Unit
) :
    RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CviewMemeberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int {
        return mMemberList.let { mMemberList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
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
        Log.v(TAG,"setItem(...) inputMembers size : ${members.size}")
        this.mMemberList.clear()
        this.mMemberList.addAll(members)
        notifyDataSetChanged()
    }
}