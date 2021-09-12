package com.khs.nbbang.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.animation.ZoomOutPageTransformer
import com.khs.nbbang.databinding.CviewHistoryListItemBinding
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.itemView.HistoryGroupFirstFragment
import com.khs.nbbang.history.itemView.HistoryGroupSecondFragment
import com.khs.nbbang.page.pager.CustomViewPagerAdapter
import com.khs.nbbang.utils.DateUtils

class HistoryRecyclerViewAdapter (val Fm: FragmentManager, val mLifecycle: Lifecycle, private val mHistoryList: List<NBBangHistory>, val mItemClick: (NBBangHistory) -> Unit) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CviewHistoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mItemClick)
    }

    override fun getItemCount(): Int {
        return mHistoryList.let { mHistoryList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mHistoryList.get(position))
    }

    inner class ViewHolder(val binding: CviewHistoryListItemBinding, val itemClick: (NBBangHistory) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(item: NBBangHistory) {
            with(binding) {
                txtDate.text = DateUtils().getDateforImg(item.date)
                viewPager.apply {
                    adapter =
                        CustomViewPagerAdapter(
                            Fm,
                            mLifecycle,
                            mutableListOf(HistoryGroupFirstFragment(item), HistoryGroupSecondFragment(item))
                        )
                    currentItem = 0
                    setPageTransformer(ZoomOutPageTransformer())
                    viewIndicator.setViewPager2(this)
                }
            }
        }
    }
}