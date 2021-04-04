package com.khs.nbbang.kakaoFriends

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddFriendsByKakaoBinding
import com.khs.nbbang.history.HistoryRecyclerViewAdapter
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.utils.ServiceUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddFriendsFragment : BaseFragment() {
    lateinit var mBinding: FragmentAddFriendsByKakaoBinding
//    val gMemberManagementViewModel : MemberManagementViewModel by sharedViewModel()
    val gLoginViewModel : LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_friends_by_kakao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = gLoginViewModel
        initView()
        addObserver()
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        val layoutManager = LinearLayoutManager(context)
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(HistoryItemDecoration(10))
            this.layoutManager = layoutManager
        }
    }

    private fun addObserver() {
        mBinding.viewModel ?: return

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //todo 팝업 기능 추가 시 hide는 여기서 처리
            }
        }
        return false
    }
}