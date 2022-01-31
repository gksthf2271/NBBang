package com.khs.nbbang.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.databinding.CviewSearchHistoryItemBinding

class SearchKeywordHistoryAdapter(
    private val mSearchResultList: ArrayList<GetSearchResult>,
    val gSearchItemClick: (GetSearchResult) -> Unit,
    val gRemoveItemClick: (GetSearchResult) -> Unit
) :
    RecyclerView.Adapter<SearchKeywordHistoryAdapter.ViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CviewSearchHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, gSearchItemClick)
    }

    override fun getItemCount(): Int {
        return mSearchResultList.run { mSearchResultList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = mSearchResultList[position]
        holder.bind(view)
    }

    inner class ViewHolder(
        val binding: CviewSearchHistoryItemBinding,
        val itemClick: (GetSearchResult) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(item: GetSearchResult) {
            with(binding) {
                txtHistoryTitle.apply {
                    text = item.kakaoSearchKeyword.keyword
                    setOnClickListener {
                        gSearchItemClick(item)
                    }
                }
                imgRemove.setOnClickListener {
                    gRemoveItemClick(item)
                }
            }
        }
    }
}