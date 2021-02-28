package com.khs.nbbang.page.adapter

import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
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
    val mItemList: ArrayList<Int>,
    val mItemClick: (Int) -> Unit,
    val mJoinBtnClick : (Int) -> Unit
) : RecyclerView.Adapter<AddPlaceRecyclerViewAdapter.PlaceViewHolder>() {
    private val TAG: String = javaClass.name
    val DEBUG = BuildConfig.DEBUG

    val TYPE_EDIT_PLACE_NAME: String = "TYPE_EDIT_PLACE_NAME"
    val TYPE_EDIT_PRICE: String = "TYPE_EDIT_PRICE"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cview_edit_place, parent, false)
        return PlaceViewHolder(view, mItemClick)
    }

    override fun getItemCount(): Int {
        if (!DEBUG) Log.v(TAG, "getItemCount : ${mItemList.size}")
        return mItemList.let { mItemList.size }
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        if (!DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
        holder.bind(mItemList.get(position), position)
    }

    inner class PlaceViewHolder(itemView: View, itemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val TAG: String = javaClass.name
        var mItemView: View = itemView
        var mItemClick = itemClick

        fun bind(placeIndex: Int, position: Int) {
            mItemView.tag = placeIndex

            mItemView.btn_join.setOnClickListener {
                mJoinBtnClick(placeIndex)
            }
            mItemView.txt_index.text = "$placeIndex 차"
            mItemView.edit_title.addTextChangedListener(
                getTextWatcher(
                    mItemView.edit_title,
                    TYPE_EDIT_PLACE_NAME,
                    placeIndex
                )
            )
            mItemView.edit_price.addTextChangedListener(
                getTextWatcher(
                    mItemView.edit_price,
                    TYPE_EDIT_PRICE,
                    placeIndex
                )
            )

            mItemView.setOnClickListener {
                mItemClick(position)
            }

            mViewModel.let {
                it!!.updateJoinPlaceCount(placeIndex)
            }

            mViewModel.mSelectedPeopleMap.observe(mObserverOwner, Observer {
                it.get(placeIndex) ?: return@Observer
                Log.v(TAG, "_selectedPeopleMap, Observer(...) : $it")
                if (it!!.get(placeIndex)!!.mMemberList.isEmpty()) {
                    hideAddedPeopleView(mItemView as ConstraintLayout)
                } else {
                    showAddedPeopleView(mItemView as ConstraintLayout, it!!.get(placeIndex)!!)
                }
            })
        }
    }

    fun updateSelectedMember(targetView : ConstraintLayout, nbb: NBB?) {
        Log.v(TAG,"updateSelectedMember(...)")
        nbb ?: return
        if (nbb.mMemberList.isEmpty()){
            hideAddedPeopleView(targetView)
        } else {
            showAddedPeopleView(targetView, nbb)
        }
    }

    fun setItem(placeIndex: Int) {
        Log.v(TAG,"setItem(...) placeIndex : ${placeIndex}")
        this.mItemList.add(mItemList.size, mItemList.last() + 1)
        notifyDataSetChanged()
    }

    fun setItemList(placeIndexList: ArrayList<Int>) {
        Log.v(TAG,"setItem(...) placeIndexList count : ${placeIndexList.size}")
        mItemList.clear()
        mItemList.addAll(placeIndexList)
        notifyDataSetChanged()
    }

    private fun showAddedPeopleView(view: ConstraintLayout, NBB: NBB) {
        Log.v(TAG, "showAddedPeopleView(...), ${view.txt_index.text}")
        view.txt_added_people.apply {
            this!!.text = StringUtils().getPeopleList(NBB.mMemberList)
            Log.v(TAG,"addedMember : ${this!!.text}")
        }
        view.layout_group_added_people.visibility = View.VISIBLE
    }

    fun hideAddedPeopleView(view: ConstraintLayout) {
        Log.v(TAG, "hideAddedPeopleView(...), ${view.txt_index}")
        view.layout_group_added_people.visibility = View.GONE
    }

    private fun getTextWatcher(view: EditText, viewType: String, placeId: Int): TextWatcher {
        return object : TextWatcherAdapter() {
            var pointNumStr = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (TYPE_EDIT_PLACE_NAME.equals(viewType)) {
                    mViewModel.savePlaceName(placeId, s.toString())
                } else if (TYPE_EDIT_PRICE.equals(viewType)) {
                    mViewModel.savePrice(placeId, s.toString())
                    if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(pointNumStr)) {
                        try {
                            pointNumStr = NumberUtils().makeCommaNumber(
                                Integer.parseInt(
                                    s.toString().replace(",", "")
                                )
                            )
                        } catch (numberFormat: NumberFormatException) {
                            Log.e(TAG, "numberFormat : $numberFormat")
                        }
                        view.setText(pointNumStr)
                        view.setSelection(pointNumStr.length)  //커서를 오른쪽 끝으로 보냄
                    }
                }
            }
        }
    }
}