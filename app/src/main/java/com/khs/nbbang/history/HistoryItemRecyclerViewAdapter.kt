package com.khs.nbbang.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.databinding.CviewHistoryListPlaceItemBinding
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils


class HistoryItemRecyclerViewAdapter (private val mHistoryResultPlaceList: ArrayList<Place>, val gItemClick: (Place) -> Unit) :
    RecyclerView.Adapter<HistoryItemRecyclerViewAdapter.ViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CviewHistoryListPlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, gItemClick)
    }

    override fun getItemCount(): Int {
        return mHistoryResultPlaceList.run { mHistoryResultPlaceList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mHistoryResultPlaceList[position])
    }

    inner class ViewHolder(
        val binding: CviewHistoryListPlaceItemBinding,
        val itemClick: (Place) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(item: Place) {
            with(binding) {
                txtGroupName.text = item.placeIndex.toString() +" 차"
                txtTitle.text = item.placeName
                txtJoinPeople.text = StringUtils().getPeopleList(item.joinPeopleList)
                txtPrice.text = NumberUtils().makeCommaNumber(true, item.price)
                root.setOnClickListener {
                    itemClick(item)
                }
            }
        }
    }
}