package com.khs.nbbang.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.user.Member
import kotlinx.android.synthetic.main.cview_history_bottom_item.view.*

class HistoryBottomRecyclerViewAdapter(private val gNameList: ArrayList<Member>) :
    RecyclerView.Adapter<HistoryBottomRecyclerViewAdapter.ViewHolder>() {
    val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var viewHolder: RecyclerView.ViewHolder? = null
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cview_history_bottom_item, parent, false)
        viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return gNameList.let { gNameList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(gNameList.get(position))
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: View = itemView

        fun bind(member: Member) {
            mItemView.txt_name.text = member.name
        }
    }
}