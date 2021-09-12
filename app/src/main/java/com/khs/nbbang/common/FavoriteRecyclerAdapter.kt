package com.khs.nbbang.common

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.databinding.CviewFavoriteRowBinding
import com.khs.nbbang.databinding.CviewFavoriteRowItemBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_favorite_row_item.view.*

class FavoriteRecyclerAdapter(
    val mItemList: ArrayList<Member>,
    val mItemClick: (Member) -> Unit
) : RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CviewFavoriteRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mItemClick)
    }

    override fun getItemCount(): Int {
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position))
    }

    inner class ViewHolder(val binding: CviewFavoriteRowItemBinding, itemClick: (Member) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName
        var mItemClick = itemClick

        fun bind(member: Member) {
            GlideUtils().drawMemberProfile(binding.imgProfile, member, null)

            binding.txtName.text = member.name
            binding.groupUserInfo.setOnClickListener {
                mItemClick(member)
            }
        }
    }

    fun setItem(item: Member) {
        Log.v(TAG,"setItem(...) item : ${item}")
        this.mItemList.add(mItemList.size, item)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: List<Member>) {
        Log.v(TAG,"setItem(...) itemList count : ${itemList.size}")
        mItemList.clear()
        mItemList.addAll(itemList)
        notifyDataSetChanged()
    }
}