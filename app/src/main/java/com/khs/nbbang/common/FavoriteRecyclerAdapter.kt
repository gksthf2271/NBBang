package com.khs.nbbang.common

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.databinding.CviewFavoriteRowItemBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils

class FavoriteRecyclerAdapter(
    private val mItemList: ArrayList<Member>,
    private val mItemClick: (Member) -> Unit
) : RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {
    private val TAG: String = javaClass.simpleName
    private val DEBUG = BuildConfig.DEBUG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = CviewFavoriteRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding, mItemClick)
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(mItemList[position])
    }

    inner class FavoriteViewHolder(
        val binding: CviewFavoriteRowItemBinding,
        val itemClick: (Member) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        val TAG: String = javaClass.simpleName

        fun bind(member: Member) {
            if (DEBUG) Log.v(TAG, "FavoriteViewHolder, bind(...) : $member")
            binding.apply {
                GlideUtils().drawMemberProfile(imgProfile, member, null)

                txtName.text = member.name
                groupUserInfo.setOnClickListener {
                    itemClick(member)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(item: Member) {
        Log.v(TAG,"setItem(...) item : ${item}")
        this.mItemList.add(mItemList.size, item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(itemList: List<Member>) {
        Log.v(TAG,"setItem(...) itemList count : ${itemList.size}")
        mItemList.clear()
        mItemList.addAll(itemList)
        notifyDataSetChanged()
    }
}