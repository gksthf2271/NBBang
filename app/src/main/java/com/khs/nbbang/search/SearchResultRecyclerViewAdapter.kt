package com.khs.nbbang.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.databinding.CviewSearchResultItemBinding
import com.khs.nbbang.search.response.DocumnetModel

class SearchResultRecyclerViewAdapter (private val mSearchResultList: ArrayList<DocumnetModel>, val gItemClick: (Boolean, DocumnetModel) -> Unit) :
    RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CviewSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, gItemClick)
    }

    override fun getItemCount(): Int {
        return mSearchResultList.run { mSearchResultList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = mSearchResultList[position]
        holder.bind(view)
    }

    inner class ViewHolder(
        val binding: CviewSearchResultItemBinding,
        val itemClick: (Boolean, DocumnetModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(item: DocumnetModel) {
            with(binding) {
                if(absoluteAdapterPosition == mSearchResultList.size - 1) {
                    divider.visibility = View.GONE
                } else {
                    divider.visibility = View.VISIBLE
                }
                val category = item.category_group_name
                if (category.isNullOrEmpty()) {
                    txtResultCategory.visibility = View.GONE
                } else {

                }
                txtResultTitle.text = item.place_name
                txtResultAddress.text = item.address_name
                txtResultCategory.text = "$category  "
                txtResultPhoneNumber.text = item.phone

                groupInfo.setOnClickListener {
                    itemClick(false, item)
                }

                imgGoMap.setOnClickListener {
                    itemClick(true, item)
                }
            }
        }
    }
}