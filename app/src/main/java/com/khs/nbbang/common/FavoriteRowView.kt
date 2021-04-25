package com.khs.nbbang.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khs.nbbang.databinding.CviewFavoriteRowBinding
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member

class FavoriteRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val TAG = this.javaClass.simpleName
    var mBinding: CviewFavoriteRowBinding
    lateinit var mRecyclerViewAdapter : FavoriteRecyclerAdapter
    lateinit var mPageViewModel : PageViewModel

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewFavoriteRowBinding.inflate(inflater, this,true)
    }

    fun initView(vm: ViewModel) {
        when (vm) {
            vm as PageViewModel -> {
                mPageViewModel = vm
                mRecyclerViewAdapter = FavoriteRecyclerAdapter(arrayListOf()) { member ->
                    Log.v(TAG,"ItemClicked, member : ${member.name}")
                    mPageViewModel.let {
                        if (!mPageViewModel.mNBBLiveData.value!!.mMemberList.contains(member)){
                            it!!.addJoinPeople(member)
                        } else {
                            Toast.makeText(context, "${member.name}은 이미 추가된 멤버입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        initRecyclerView()
    }

    fun initRecyclerView() {
        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            isFocusable = true
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            val listener = object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val action = e.action
                    if (canScrollHorizontally(RecyclerView.FOCUS_FORWARD)) {
                        when (action) {
                            MotionEvent.ACTION_MOVE -> rv.parent
                                .requestDisallowInterceptTouchEvent(true)
                        }
                        return false
                    }
                    else {
                        when (action) {
                            MotionEvent.ACTION_MOVE -> rv.parent
                                .requestDisallowInterceptTouchEvent(false)
                        }
                        removeOnItemTouchListener(this)
                        return true
                    }
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            }

            addOnItemTouchListener(listener)
            adapter = mRecyclerViewAdapter
        }
    }

    fun setTitle(title: String) {
        mBinding.let {
            mBinding.txtTitle.text = title
        }
    }

    fun setList(itemList : List<Member>){
        mRecyclerViewAdapter.setItemList(itemList)
    }
}