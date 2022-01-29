package com.khs.nbbang.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.databinding.CviewFavoriteRowBinding
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.LogUtil
import com.khs.nbbang.utils.ScrollUtils
import java.util.*

class FavoriteRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_UI
    var mBinding: CviewFavoriteRowBinding
    var mRecyclerViewAdapter : FavoriteRecyclerAdapter? = null
    var mPageViewModel : PageViewModel? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewFavoriteRowBinding.inflate(inflater, this, true).apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                isFocusable = true
                descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            }
        }
    }

    fun initView(vm: ViewModel, itemList: List<Member>) {
        if (vm is PageViewModel) {
            mPageViewModel = vm
            mRecyclerViewAdapter = FavoriteRecyclerAdapter(ArrayList(itemList)) { member ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "ItemClicked, member : ${member.name}")
                mPageViewModel?.let { pageViewModel ->
                    if (!pageViewModel.mNBBLiveData.value!!.mMemberList.contains(member)){
                        pageViewModel.addJoinPeople(member)
                    } else {
                        Toast.makeText(context, "${member.name}은 이미 추가된 멤버입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (vm is LoginViewModel) {
            mRecyclerViewAdapter = FavoriteRecyclerAdapter(ArrayList(itemList)) { member ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "ItemClicked, member : ${member.name}")
            }
            LogUtil.vLog(LOG_TAG, TAG_CLASS, "FavoriteRowView Viewmodel is LoginViewModel")
        }

        mBinding.recyclerView.adapter= mRecyclerViewAdapter
    }

    fun setControlScrolling() {
        ScrollUtils.controlHorizontalScrollingInViewPager2(mBinding.recyclerView, rootView.findViewById(R.id.view_pager))
    }

    fun setTitle(title: String) {
        mBinding.txtTitle.text = title
    }

    fun setList(itemList : List<Member>){
        mRecyclerViewAdapter?.setItemList(itemList)
    }
}