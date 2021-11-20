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
import com.khs.nbbang.R
import com.khs.nbbang.databinding.CviewFavoriteRowBinding
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.ScrollUtils
import kotlinx.android.synthetic.main.fragment_dutchpay_home.view.*

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
        if (vm is PageViewModel) {
            mPageViewModel = vm
            mRecyclerViewAdapter = FavoriteRecyclerAdapter(arrayListOf()) { member ->
                Log.v(TAG,"ItemClicked, member : ${member.name}")
                mPageViewModel?.let {
                    if (!mPageViewModel.mNBBLiveData.value!!.mMemberList.contains(member)){
                        it.addJoinPeople(member)
                    } else {
                        Toast.makeText(context, "${member.name}은 이미 추가된 멤버입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (vm is LoginViewModel) {
            mRecyclerViewAdapter = FavoriteRecyclerAdapter(arrayListOf()) { member ->
                Log.v(TAG,"ItemClicked, member : ${member.name}")
            }
            Log.v(TAG,"FavoriteRowView Viewmodel is LoginViewModel")
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            isFocusable = true
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            adapter = mRecyclerViewAdapter
        }
    }

    fun setControlScrolling() {
        ScrollUtils.controlHorizontalScrollingInViewPager2(mBinding.recyclerView, rootView.findViewById(R.id.view_pager))
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