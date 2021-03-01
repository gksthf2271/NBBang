package com.khs.nbbang.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.databinding.CviewFavoriteRowBinding
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member

class FavoriteRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val TAG = this.javaClass.name
    var mBinding: CviewFavoriteRowBinding
    lateinit var mRecyclerViewAdapter : FavoriteRecyclerAdapter
    lateinit var mPageViewModel : PageViewModel

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewFavoriteRowBinding.inflate(inflater, this,true)
    }

    fun initView(vm : PageViewModel){
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

        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            isFocusable = true
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
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