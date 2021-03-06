package com.khs.nbbang.common

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_favorite_row_item.view.*

class FavoriteRecyclerAdapter(
    val mItemList: ArrayList<Member>,
    val mItemClick: (Member) -> Unit
) : RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder>() {
    private val TAG: String = javaClass.name
    val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cview_favorite_row_item, parent, false)
        return ViewHolder(view, mItemClick)
    }

    override fun getItemCount(): Int {
        if (DEBUG) Log.v(TAG, "getItemCount : ${mItemList.size}")
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position), position)
    }

    inner class ViewHolder(itemView: View, itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.name
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(member: Member, position: Int) {
            GlideUtils().drawMemberProfile(mItemView.img_profile, member, null)

            mItemView.txt_name.text = member.name
            mItemView.group_user_info.setOnClickListener {
                mItemClick(member)
            }
        }
    }

    fun setItem(item: Member) {
        Log.v(TAG,"setItem(...) item : ${item}")
        this.mItemList.add(mItemList.size, item)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: List<Member>) {
        Log.v(TAG,"setItem(...) itemList count : ${itemList.size}")
        mItemList.clear()
        mItemList.addAll(itemList)
        notifyDataSetChanged()
    }
}