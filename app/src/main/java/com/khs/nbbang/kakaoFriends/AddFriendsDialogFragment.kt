package com.khs.nbbang.kakaoFriends

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.common.FavoriteRecyclerAdapter
import com.khs.nbbang.databinding.FragmentAddFriendsByKakaoBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DebugMemberList
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AddFriendsDialogFragment : BaseDialogFragment(DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS) {
    lateinit var mBinding: FragmentAddFriendsByKakaoBinding
    private val gMemberManagementViewModel : MemberManagementViewModel by sharedViewModel()
    private val gLoginViewModel : LoginViewModel by sharedViewModel()
    private val DEBUG = true

    lateinit var mRecyclerViewAdapter : FavoriteRecyclerAdapter

    companion object {
        @Volatile
        private var instance: AddFriendsDialogFragment? = null

        @JvmStatic
        fun getInstance(): AddFriendsDialogFragment =
            instance ?: synchronized(this) {
                instance
                    ?: AddFriendsDialogFragment().also {
                        instance = it
                    }
            }
    }

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

    fun initView() {
        val layoutManager = GridLayoutManager(context, 3)

        mBinding.btnClose.setOnClickListener { if (this.isAdded) dismiss() }

        mBinding.allFriendsRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(HistoryItemDecoration(20))
            this.layoutManager = layoutManager
        }

        mRecyclerViewAdapter = FavoriteRecyclerAdapter(arrayListOf()) { member ->
            Log.v(TAG,"ItemClicked, member : ${member.name}")

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
                Log.v(TAG, "loadFriends result : ${it.joinToString("\n")}")
                var memberArrayList = arrayListOf<Member>()
                if (DEBUG) {
                    memberArrayList.addAll(DebugMemberList.mDummyMemberList)
                } else {
                    memberArrayList.addAll(it.map {
                        Member(
                            id = it.id,
                            index = it.index,
                            name = it.profileNickname,
                            groupId = it.groupId,
                            description = it.description,
                            kakaoId = it.uuId,
                            thumbnailImage = it.thumbnailImage,
                            isFavorite = it.isFavorite,
                            isFavoriteByKakao = it.isFavoriteByKakao
                        )
                    })

                }

                mBinding.allFriendsRecyclerView.adapter =
                    AddFriendsRecyclerViewAdapter(memberArrayList) {
                        Log.v(TAG, "ItemClicked : $it")
                        gMemberManagementViewModel.selectMember(it)
                    }
            })
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (this.isAdded) {
                    dismiss()
                    return true
                }
            }
        }
        return false
    }
}