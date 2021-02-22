package com.khs.nbbang.group

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.R
import com.khs.nbbang.user.Member
import com.khs.nbbang.user.User
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_memeber_item.view.*


class MemberRecyclerViewAdapter(
    private val mContext: Context,
    private val mMemberList: List<Member>,
    private val itemClick: (Member) -> Unit
) :
    RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.name
    val DEBUG = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cview_memeber_item, parent, false)
        viewHolder = ViewHolder(view, itemClick)
        return viewHolder
    }

    override fun getItemCount(): Int {
        if(DEBUG) Log.v(TAG,"getItemCount : ${mMemberList.size}")
        return mMemberList.let { mMemberList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mMemberList.get(position))
    }

    inner class ViewHolder(itemView: View, itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.name
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(member: Member) {
            mItemView.txt_name.text = member.name
            mItemView.txt_description.text = member.description
            GlideUtils().drawImageWith(
                mContext, mItemView.img_profile
                , null, null
            )
            mItemView.setOnClickListener { mItemClick(member) }
        }
    }
}