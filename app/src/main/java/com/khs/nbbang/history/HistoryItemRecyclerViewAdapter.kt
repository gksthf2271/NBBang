package com.khs.nbbang.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_history_list_place_item.view.*


class HistoryItemRecyclerViewAdapter (private val mHistoryResultPlaceList: ArrayList<Place>, val itemClick: (NBBangHistory) -> Unit) :
    RecyclerView.Adapter<HistoryItemRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cview_history_list_place_item, parent, false)
        viewHolder = ViewHolder(view, itemClick)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.v(TAG,"getItemCount : ${mHistoryResultPlaceList.size}")
        return mHistoryResultPlaceList.let { mHistoryResultPlaceList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mHistoryResultPlaceList.get(position))
    }

    inner class ViewHolder(itemView: View, itemClick: (NBBangHistory) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: View = itemView

        fun bind(item: Place) {
            mItemView.txt_group_name.text = item.placeIndex.toString() +" 차"
            mItemView.txt_title.text = item.placeName
            mItemView.txt_join_people.text = StringUtils().getPeopleList(item.joinPeopleList)
            mItemView.txt_price.text = NumberUtils().makeCommaNumber(true, item.price)
        }
    }
}