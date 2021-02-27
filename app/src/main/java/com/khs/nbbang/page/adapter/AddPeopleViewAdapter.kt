package com.khs.nbbang.page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.page.PeopleNameWatcherCallback
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DisplayUtils
import kotlinx.android.synthetic.main.cview_edit_people.view.*

class AddPeopleViewAdapter(context: Context, itemList: MutableList<Member>, callback: PeopleNameWatcherCallback) : BaseAdapter() {
    val TAG = this.javaClass.name
    var mItemList: MutableList<Member>
    var mItemView : MutableList<View>
    var mContext: Context
    val mCallback : PeopleNameWatcherCallback

    init {
        mItemList = itemList
        mContext = context
        mItemView = mutableListOf()
        mCallback = callback
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (mItemView.size <= position) {
            if (position == 0) {
                mItemView.add(position,addPlusView(parent))
            } else {
                mItemView.add(position,addPeopleView(parent, position))
            }
        }
        return mItemView.get(position)
    }

    private fun addPlusView(parent: ViewGroup?) : View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: ConstraintLayout =
            inflater.inflate(R.layout.cview_edit_people_plus, parent, false) as ConstraintLayout
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)

        return itemView
    }

    private fun addPeopleView(parent: ViewGroup?, position: Int) : View {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView: ConstraintLayout =
            inflater.inflate(R.layout.cview_edit_people, parent, false) as ConstraintLayout
        var viewSize = DisplayUtils().getItemViewSize(mContext, 3)

        itemView!!.layoutParams = ConstraintLayout.LayoutParams(viewSize, viewSize)
        itemView.txt_name.setText(mItemList.get(position).name)
        itemView.txt_name.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                mCallback.onCallback(position,s.toString())
            }
        })
        return itemView
    }

    override fun getItem(position: Int): Any {
        return mItemList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return mItemList.get(position).index.toLong()
    }

    override fun getCount(): Int {
        return mItemList.size
    }

    fun addItem(member: Member) {
        mItemList.add(member)
    }

    fun addItem(index: Int, member: Member) {
        mItemList.add(index, member)
        notifyDataSetChanged()
    }
}