package com.khs.nbbang.group

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.user.User
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_memeber_item.view.*


class MemberRecyclerViewAdapter(
    private val mContext: Context,
    private val mPeopleList: List<User>,
    private val itemClick: (User) -> Unit
) :
    RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.name
    val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cview_memeber_item, parent, false)
        viewHolder = ViewHolder(view, itemClick)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.v(TAG,"getItemCount : ${mPeopleList.size}")
        return mPeopleList.let { mPeopleList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mPeopleList.get(position))
    }

    inner class ViewHolder(itemView: View, itemClick: (User) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.name
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(user: User) {
            mItemView.txt_name.text = user.name
            mItemView.txt_description.text = user.id.toString()
            GlideUtils().drawImageWith(
                mContext, mItemView.img_profile
                , null, null
            )
            mItemView.setOnClickListener { mItemClick(user) }
        }
    }
}