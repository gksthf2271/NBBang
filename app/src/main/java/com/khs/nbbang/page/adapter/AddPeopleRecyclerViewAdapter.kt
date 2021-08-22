package com.khs.nbbang.page.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DisplayUtils
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_edit_people.view.*

class AddPeopleRecyclerViewAdapter(
    val mContext: Context,
    val mItemList: ArrayList<Member>,
    val mItemClick: (Pair<Int, Member>) -> Unit
) : RecyclerView.Adapter<AddPeopleRecyclerViewAdapter.PeopleViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view: View? = LayoutInflater.from(parent.context).inflate(R.layout.cview_edit_people, parent, false)
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)
        view!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        return PeopleViewHolder(view, mItemClick)
    }

    override fun getItemCount(): Int {
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position), position)
    }

    inner class PeopleViewHolder(itemView: View, itemClick: (Pair<Int, Member>) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(member: Member, position: Int) {
            mItemView.txt_name.text = member.name
            GlideUtils().drawMemberProfile(mItemView.img_profile, member, null)
            mItemView.setOnClickListener {
                mItemClick(Pair(position, member))
            }
        }
    }

    fun setItem(member: Member) {
        Log.v(TAG,"setItem(...) member : ${member}")
        this.mItemList.add(mItemList.size, member)
        notifyDataSetChanged()
    }

    fun setItemList(memberList: ArrayList<Member>) {
        Log.v(TAG,"setItem(...) memberList count : ${memberList.size}")
        mItemList.clear()
        mItemList.addAll(memberList)
        notifyDataSetChanged()
    }
}