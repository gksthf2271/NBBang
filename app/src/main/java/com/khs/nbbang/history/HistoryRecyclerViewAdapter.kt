package com.khs.nbbang.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.animation.ZoomOutPageTransformer
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.itemView.HistoryGroupFirstFragment
import com.khs.nbbang.history.itemView.HistoryGroupSecondFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import com.khs.nbbang.utils.DateUtils
import kotlinx.android.synthetic.main.cview_history_list_item.view.*

class HistoryRecyclerViewAdapter (val Fm: FragmentManager, val mLifecycle: Lifecycle, private val mHistoryList: List<NBBangHistory>, val itemClick: (NBBangHistory) -> Unit) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cview_history_list_item, parent, false)
        viewHolder = ViewHolder(view, itemClick)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return mHistoryList.let { mHistoryList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mHistoryList.get(position))
    }

    inner class ViewHolder(itemView: View, itemClick: (NBBangHistory) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: View = itemView

        fun bind(item: NBBangHistory) {
            mItemView.txt_date.text = DateUtils().getDateforImg(item.date)
            mItemView.view_pager.apply {
                adapter =
                    CustomViewPagerAdapter(
                        Fm,
                        mLifecycle,
                        mutableListOf(HistoryGroupFirstFragment(item), HistoryGroupSecondFragment(item))
                    )
                currentItem = 0
                setPageTransformer(ZoomOutPageTransformer())
                mItemView.view_indicator.setViewPager2(this)
            }
        }
    }
}