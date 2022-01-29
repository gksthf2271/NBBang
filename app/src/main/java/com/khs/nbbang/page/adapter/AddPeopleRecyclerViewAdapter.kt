package com.khs.nbbang.page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.databinding.CviewEditPeopleBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DisplayUtils
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.LogUtil

class AddPeopleRecyclerViewAdapter(
    private val mContext: Context,
    private val mItemList: ArrayList<Member>,
    private val mItemClick: (Pair<Int, Member>) -> Unit
) : RecyclerView.Adapter<AddPeopleRecyclerViewAdapter.PeopleViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName
    private val LOG_TAG: String = LogUtil.TAG_UI
    val DEBUG = BuildConfig.DEBUG


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val binding = CviewEditPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewSize = DisplayUtils().getItemViewSize(mContext, 3)
        binding.root.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        return PeopleViewHolder(binding, mItemClick)
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(mItemList[position], position)
    }

    inner class PeopleViewHolder(
        val binding: CviewEditPeopleBinding,
        val itemClick: (Pair<Int, Member>) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(member: Member, position: Int) {
            binding.txtName.text = member.name
            GlideUtils().drawMemberProfile(binding.imgProfile, member, null)
            binding.root.setOnClickListener {
                itemClick(Pair(position, member))
            }
        }
    }

    fun setItem(member: Member) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) member : ${member}")
        this.mItemList.add(mItemList.size, member)
        notifyDataSetChanged()
    }

    fun setItemList(memberList: ArrayList<Member>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) memberList count : ${memberList.size}")
        mItemList.clear()
        mItemList.addAll(memberList)
        notifyDataSetChanged()
    }
}