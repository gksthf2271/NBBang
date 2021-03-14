package com.khs.nbbang.page.adapter

import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.utils.NumberUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_edit_place.view.*

class AddPlaceRecyclerViewAdapter(
    val mObserverOwner : LifecycleOwner,
    val mViewModel : PageViewModel,
    val mItemList: ArrayList<NBB>,
    val mItemClick: (NBB) -> Unit,
    val mJoinBtnClick : (NBB) -> Unit
) : RecyclerView.Adapter<AddPlaceRecyclerViewAdapter.PlaceViewHolder>() {
    private val TAG: String = javaClass.simpleName
    val DEBUG = BuildConfig.DEBUG

    val TYPE_EDIT_PLACE_NAME: String = "TYPE_EDIT_PLACE_NAME"
    val TYPE_EDIT_PRICE: String = "TYPE_EDIT_PRICE"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cview_edit_place, parent, false)
        return PlaceViewHolder(view, mItemClick)
    }

    override fun getItemCount(): Int {
        if (DEBUG) Log.v(TAG, "getItemCount : ${mItemList.size}")
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position), position)
    }

    inner class PlaceViewHolder(itemView: View, itemClick: (NBB) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.simpleName
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(nbb: NBB, position: Int) {
            mItemView.tag = nbb.mPlaceIndex

            mItemView.btn_join.setOnClickListener {
                mJoinBtnClick(nbb)
            }
            mItemView.txt_index.text = "${nbb.mPlaceIndex} 차"

            mItemView.setOnClickListener {
                mItemClick(nbb)
            }

            mViewModel.let {
                it!!.updateJoinPlaceCount(nbb.mPlaceIndex)
            }

            if (mItemView !is MotionLayout) return
            when {
                nbb.mMemberList.isEmpty() -> hideAddedPeopleView(mItemView as MotionLayout)
                else -> showAddedPeopleView(mItemView as MotionLayout, nbb)
            }
        }
    }

    fun setItem(nbb: NBB) {
        Log.v(TAG,"setItem(...) placeIndex : ${nbb.mPlaceIndex}")
        this.mItemList.add(mItemList.size, nbb)
        notifyDataSetChanged()
    }

    fun setItemList(nbbList: List<NBB>) {
        Log.v(TAG,"setItem(...) placeIndexList count : ${nbbList.size}")
        mItemList.clear()
        mItemList.addAll(nbbList)
        notifyDataSetChanged()
    }

    private fun showAddedPeopleView(view: MotionLayout, nbb: NBB) {
        Log.v(TAG, "showAddedPeopleView(...), ${view.txt_index.text}")
        view.txt_added_people.text = StringUtils().getPeopleList(nbb.mMemberList)
        Log.v(TAG,"addedMember : ${view.txt_added_people.text}")

//        view.motion_layout.setTransition(R.id.motion_place_item)
//        view.motion_layout.transitionToEnd()
    }

    fun hideAddedPeopleView(view: MotionLayout) {
        Log.v(TAG, "hideAddedPeopleView(...), ${view.txt_index}")
//        view.motion_layout.setTransition(R.id.motion_place_item)
//        view.motion_layout.transitionToStart()
    }
}