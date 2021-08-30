package com.khs.nbbang.kakaoFriends

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentAddFriendsByKakaoBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.SelectMemberViewModel
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DebugMemberList
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AddFriendsDialogFragment : BaseDialogFragment(DIALOG_TYPE.TYPE_ADD_KAKAO_FIRENDS) {
    lateinit var mBinding: FragmentAddFriendsByKakaoBinding
    private val gMemberManagementViewModel : MemberManagementViewModel by sharedViewModel()
    private val gLoginViewModel : LoginViewModel by sharedViewModel()
    private val gSelectMemberViewModel by viewModel<SelectMemberViewModel>()
    private val DEBUG = false

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

        mBinding.rowFavoriteMember.initView(mBinding.viewModel!!)
        mBinding.rowFavoriteMember.setTitle("Favorite Member")

        mBinding.btnClose.setOnClickListener { if (this.isAdded) dismiss() }
        mBinding.btnSave.setOnClickListener {
            gMemberManagementViewModel.saveKakaoMember(gSelectMemberViewModel.getSelectedMemberHashMap().values.toList())
            dismiss()
        }

        mBinding.allFriendsRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(HistoryItemDecoration(20))
            this.layoutManager = layoutManager
        }

        mBinding.viewModel.let { it!!.loadFriendList() }
    }

    private fun addObserver() {
        mBinding.viewModel.let { loginViewModel ->
            loginViewModel!!.gIsLogin.observe(requireActivity(), Observer {
                if (!it) {
                    Log.e(TAG, "isLogin : ${it}")
                    return@Observer
                }
            })
            loginViewModel!!.gFriendList.observe(requireActivity(), Observer { it ->
                Log.v(TAG, "KakaoFriends remote list : ${it.joinToString("\n")}")
                val memberArrayList = arrayListOf<Member>()
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

                val remoteMemberHashMap = hashMapOf<String, Member>()
                for (member in memberArrayList) {
                    remoteMemberHashMap[member.kakaoId] = member
                }


                gMemberManagementViewModel.gKakaoFriendList.observe(requireActivity(), Observer {
                    Log.v(TAG, "KakaoFriends Local list : ${it.joinToString("\n")}")
                    val localMemberArrayList = arrayListOf<Member>()
                    if (DEBUG) {
                        localMemberArrayList.addAll(DebugMemberList.mDummyMemberList)
                    } else {
                        localMemberArrayList.addAll(it)
                    }
                    val memberHashMap = hashMapOf<String, Member>()
                    for (member in localMemberArrayList) {
                        memberHashMap[member.kakaoId] = member
                    }

                    gSelectMemberViewModel.setSelectedMemberList(memberHashMap)

                    mBinding.allFriendsRecyclerView.adapter =
                        AddFriendsRecyclerViewAdapter(remoteMemberHashMap, memberHashMap, {
                            Log.v(TAG, "ItemClicked : $it")
                        }, { isSaveCallback, member ->
                            when {
                                isSaveCallback -> {
                                    gSelectMemberViewModel.addSelectedMember(member)
                                }
                                else -> {
                                    gSelectMemberViewModel.removeSelectedMember(member)
                                }
                            }
                        })
                })
            })
        }

        gSelectMemberViewModel.gSelectedMemberHashMap.observe(requireActivity(), Observer { memberMap ->
            Log.v(TAG,"update SelectedMember list! : $memberMap")
            val result : ArrayList<Member> = arrayListOf()
            val myData = gLoginViewModel.gMyData.value
            myData?.id.let {
                for (member in memberMap!!.values) {
                    if (it!!.toString() != member.kakaoId) {
                        result.add(member)
                    }
                }
            }
            mBinding.rowFavoriteMember.setList(result)
        })
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