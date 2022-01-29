package com.khs.nbbang.common

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.databinding.CviewFavoriteRowItemBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.LogUtil

class FavoriteRecyclerAdapter(
    private val mItemList: ArrayList<Member>,
    private val mItemClick: (Member) -> Unit
) : RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {
    private val TAG_CLASS: String = javaClass.simpleName
    private val LOG_TAG: String = LogUtil.TAG_UI
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

        fun bind(member: Member) {
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
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) item : ${item}")
        this.mItemList.add(mItemList.size, item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(itemList: List<Member>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setItem(...) itemList count : ${itemList.size}")
        mItemList.clear()
        mItemList.addAll(itemList)
        notifyDataSetChanged()
    }
}