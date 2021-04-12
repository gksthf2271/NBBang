package com.khs.nbbang.kakaoFriends

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentAddFriendsByKakaoBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddFriendsFragment : BaseFragment() {
    lateinit var mBinding: FragmentAddFriendsByKakaoBinding
    private val gMemberManagementViewModel : MemberManagementViewModel by sharedViewModel()
    private val gLoginViewModel : LoginViewModel by sharedViewModel()

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
        mBinding.viewModel.let { it!!.loadFriendList() }
    }

    private fun addObserver() {
        mBinding.viewModel.let { loginViewModel ->
            loginViewModel!!.gIsLogin.observe(requireActivity(), Observer {
                if (!it) {
                    Log.e(TAG,"isLogin : ${it}")
                    return@Observer
                }
            })
            loginViewModel!!.gFriendList.observe(requireActivity(), Observer {
                Log.v(TAG,"loadFriends result : ${it.joinToString("\n")}")
            })
        }
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