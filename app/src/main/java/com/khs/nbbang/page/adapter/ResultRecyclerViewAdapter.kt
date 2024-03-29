package com.khs.nbbang.page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.databinding.CviewShareResultItemBinding
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.utils.LogUtil
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils

class ResultRecyclerViewAdapter(
    val mItemList: ArrayList<Place>,
    val mItemClick: (Place) -> Unit
) : RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName
    private val LOG_TAG: String = LogUtil.TAG_UI
    val DEBUG = BuildConfig.DEBUG


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CviewShareResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mItemClick)
    }

    override fun getItemCount(): Int {
        return mItemList.run { this.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mItemList.get(position))
    }

    inner class ViewHolder(val binding: CviewShareResultItemBinding, val itemClick: (Place) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(place: Place) {
            binding.apply {
                txtPlaceName.text = place.placeName
                txtPlaceIndex.text = "${place.placeIndex} 차"
                txtPrice.text = NumberUtils().makeCommaNumber(true, place.price)
                txtJoinPeople.text = StringUtils.getPeopleList(place.joinPeopleList)
            }
            binding.root.setOnClickListener {
                itemClick(place)
            }
        }
    }

    fun setItem(place: Place) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) place : ${place}")
        this.mItemList.add(mItemList.size, place)
        notifyDataSetChanged()
    }

    fun setItemList(plcaeList: ArrayList<Place>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) plcaeList count : ${plcaeList.size}")
        mItemList.clear()
        mItemList.addAll(plcaeList)
        notifyDataSetChanged()
    }
}