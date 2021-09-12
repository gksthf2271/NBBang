//package com.khs.nbbang.page.adapter
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.constraintlayout.motion.widget.MotionLayout
//import androidx.lifecycle.LifecycleOwner
//import androidx.recyclerview.widget.RecyclerView
//import com.khs.nbbang.BuildConfig
//import com.khs.nbbang.databinding.CviewEditPlaceBinding
//import com.khs.nbbang.page.ItemObj.NBB
//import com.khs.nbbang.page.viewModel.PageViewModel
//import com.khs.nbbang.utils.StringUtils
//import kotlinx.android.synthetic.main.cview_edit_place.view.*
//
//class AddPlaceRecyclerViewAdapter(
//    val mObserverOwner : LifecycleOwner,
//    val mViewModel : PageViewModel,
//    val mItemList: ArrayList<NBB>,
//    val mItemClick: (NBB) -> Unit,
//    val mJoinBtnClick : (NBB) -> Unit
//) : RecyclerView.Adapter<AddPlaceRecyclerViewAdapter.PlaceViewHolder>() {
//    private val TAG: String = javaClass.simpleName
//    val DEBUG = BuildConfig.DEBUG
//
//    val TYPE_EDIT_PLACE_NAME: String = "TYPE_EDIT_PLACE_NAME"
//    val TYPE_EDIT_PRICE: String = "TYPE_EDIT_PRICE"
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
//        val binding = CviewEditPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return PlaceViewHolder(binding, mItemClick)
//    }
//
//    override fun getItemCount(): Int {
//        return mItemList.let { mItemList.size }
//    }
//
//    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
//        if (DEBUG) Log.v(TAG, "onBindViewHolder, position : $position")
//        holder.bind(mItemList.get(position))
//    }
//
//    inner class PlaceViewHolder(val binding: CviewEditPlaceBinding, itemClick: (NBB) -> Unit) :
//        RecyclerView.ViewHolder(binding.root) {
//        val TAG: String = javaClass.simpleName
//
//        fun bind(nbb: NBB) {
//            val view = binding.root
//            binding.root.tag = nbb.mPlaceIndex
//
//            binding.btnJoin.setOnClickListener {
//                mJoinBtnClick(nbb)
//            }
//            binding.txtIndex.text = "${nbb.mPlaceIndex} 차"
//
//            view.setOnClickListener {
//                mItemClick(nbb)
//            }
//
//            mViewModel.let {
//                it.updateJoinPlaceCount(nbb.mPlaceIndex)
//            }
//
//            if (view !is MotionLayout) return
//            when {
//                nbb.mMemberList.isEmpty() -> hideAddedPeopleView(view as MotionLayout)
//                else -> showAddedPeopleView(view as MotionLayout, nbb)
//            }
//        }
//    }
//
//    fun setItem(nbb: NBB) {
//        Log.v(TAG,"setItem(...) placeIndex : ${nbb.mPlaceIndex}")
//        this.mItemList.add(mItemList.size, nbb)
//        notifyDataSetChanged()
//    }
//
//    fun setItemList(nbbList: List<NBB>) {
//        Log.v(TAG,"setItem(...) placeIndexList count : ${nbbList.size}")
//        mItemList.clear()
//        mItemList.addAll(nbbList)
//        notifyDataSetChanged()
//    }
//
//    private fun showAddedPeopleView(view: MotionLayout, nbb: NBB) {
//        Log.v(TAG, "showAddedPeopleView(...), ${view.txt_index.text}")
//        view.txt_added_people.text = StringUtils().getPeopleList(nbb.mMemberList)
//        Log.v(TAG,"addedMember : ${view.txt_added_people.text}")
//
////        view.motion_layout.setTransition(R.id.motion_place_item)
////        view.motion_layout.transitionToEnd()
//    }
//
//    fun hideAddedPeopleView(view: MotionLayout) {
//        Log.v(TAG, "hideAddedPeopleView(...), ${view.txt_index}")
////        view.motion_layout.setTransition(R.id.motion_place_item)
////        view.motion_layout.transitionToStart()
//    }
//}