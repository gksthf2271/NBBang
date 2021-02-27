package com.khs.nbbang.page.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.BuildConfig
import com.khs.nbbang.R
import com.khs.nbbang.page.ItemObj.JoinPeople
import com.khs.nbbang.utils.DisplayUtils
import kotlinx.android.synthetic.main.cview_edit_people.view.*

class AddPeopleRecyclerViewAdapter(
    val mContext: Context,
    val mItemList: ArrayList<JoinPeople>,
    val mItemClick: (Pair<Int, JoinPeople>) -> Unit
) : RecyclerView.Adapter<AddPeopleRecyclerViewAdapter.PeopleViewHolder>() {
    private val TAG: String = javaClass.name
    val DEBUG = BuildConfig.DEBUG

//    private val TYPE_NONE = 0
//    private val TYPE_ADD = 1
//    private val TYPE_PEOPLE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view: View? = LayoutInflater.from(parent.context).inflate(R.layout.cview_edit_people, parent, false)
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)
        view!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        return PeopleViewHolder(view, mItemClick)
//        return when (viewType) {
//            TYPE_NONE -> {
//                throw RuntimeException("TYPE : ${TYPE_NONE}, 알 수 없는 뷰 타입 에러")
//            }
//            TYPE_ADD -> {
//                view =
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.cview_edit_people_plus, parent, false)
//
//                var viewSize = DisplayUtils().getItemViewSize(mContext, 3)
//                view!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
//                AddPeopleViewHolder(view, mItemClick)
//            }
//            TYPE_PEOPLE -> {
//                view =
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.cview_edit_people, parent, false)
//                var viewSize = DisplayUtils().getItemViewSize(mContext, 3)
//                view!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
//                PeopleViewHolder(view, mItemClick)
//            }
//            else -> throw RuntimeException("알 수 없는 뷰 타입 에러")
//        }
    }

//    override fun getItemViewType(position: Int): Int {
//        var viewType = TYPE_NONE
//        if (position == 0) {
//            viewType = TYPE_ADD
//        } else if (position > 0) {
//            viewType = TYPE_PEOPLE
//        }
//        return viewType
//    }

    override fun getItemCount(): Int {
        Log.v(TAG, "getItemCount : ${mItemList.size}")
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position), position)
//        if (holder is AddPeopleViewHolder) {
//            (holder as AddPeopleViewHolder).bind(mItemList.get(position), position)
//        } else {
//            (holder as PeopleViewHolder).bind(mItemList.get(position), position)
//        }
    }


//    inner class AddPeopleViewHolder(itemView: View, itemClick: (Pair<Int, People>) -> Unit) : RecyclerView.ViewHolder(itemView) {
//        val TAG: String = javaClass.name
//        var mItemView: View = itemView
//        var mItemClick = itemClick
//
//        fun bind(people: People, position: Int) {
//            mItemView.setOnClickListener { mItemClick(Pair(position, people)) }
//        }
//    }

    inner class PeopleViewHolder(itemView: View, itemClick: (Pair<Int, JoinPeople>) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.name
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(joinPeople: JoinPeople, position: Int) {
            mItemView.txt_name.text = joinPeople.name
            mItemView.setOnClickListener {
                mItemClick(Pair(position, joinPeople))
            }
        }
    }

    fun setItem(joinPeople: JoinPeople) {
        Log.v(TAG,"setItem(...) joinPeople : ${joinPeople}")
        this.mItemList.add(mItemList.size, joinPeople)
        notifyDataSetChanged()
    }

    fun setItemList(joinPeopleList: ArrayList<JoinPeople>) {
        Log.v(TAG,"setItem(...) joinPeople count : ${joinPeopleList.size}")
        mItemList.clear()
        mItemList.addAll(joinPeopleList)
        notifyDataSetChanged()
    }
}