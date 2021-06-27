package com.khs.nbbang.page.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_share_result_item.view.*

class ResultRecyclerViewAdapter(
    val mItemList: ArrayList<Place>,
    val mItemClick: (Place) -> Unit
) : RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View? = LayoutInflater.from(parent.context).inflate(R.layout.cview_share_result_item, parent, false)
        return ViewHolder(view!!, mItemClick)
    }

    override fun getItemCount(): Int {
        Log.v(TAG, "getItemCount : ${mItemList.size}")
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position), position)
    }

    inner class ViewHolder(itemView: View, itemClick: (Place) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(place: Place, position: Int) {
            mItemView.run {
                txt_place_name.text = place.placeName
                txt_place_index.text = "${place.placeIndex} 차"
                txt_price.text = NumberUtils().makeCommaNumber(true, place.price)
                txt_join_people.text = StringUtils().getPeopleList(place.joinPeopleList)
            }
            mItemView.setOnClickListener {
                mItemClick(place)
            }
        }
    }

    fun setItem(place: Place) {
        Log.v(TAG,"setItem(...) place : ${place}")
        this.mItemList.add(mItemList.size, place)
        notifyDataSetChanged()
    }

    fun setItemList(plcaeList: ArrayList<Place>) {
        Log.v(TAG,"setItem(...) plcaeList count : ${plcaeList.size}")
        mItemList.clear()
        mItemList.addAll(plcaeList)
        notifyDataSetChanged()
    }
}