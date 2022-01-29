package com.khs.nbbang.kakaoFriends

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.databinding.FragmentKakaoFavoriteFriendsBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.utils.LogUtil
import kotlinx.android.synthetic.main.cview_page_title.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class KakaoFavoriteFriendsFragment : BaseFragment() {
    lateinit var mBinding : FragmentKakaoFavoriteFriendsBinding
    private val gMemberViewModel: MemberManagementViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentKakaoFavoriteFriendsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = gMemberViewModel
        initView()
        addObserver()
    }

    override fun makeCustomLoadingView(): Dialog? {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "makeCustomLoadingView(...)")
        return null
    }

    private fun initView() {
        if (mBinding.recyclerFriendList.adapter == null) {
            mBinding.recyclerFriendList.apply {
                layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                adapter =
                    AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                        LogUtil.vLog(LOG_TAG, TAG_CLASS, "ItemClicked : $it")
//                        mBinding.viewModel!!.selectMember(it)
                    }
            }
            addObserver()
        }
        mBinding.viewModel?.let {
            mBinding.groupTitle.txtTitle.text =
                "Kakao Favorite Member"
            it.showFavoriteMemberListByType(MemberType.TYPE_KAKAO)
        }

        mBinding.btnAdd.setOnClickListener {
            showAddKakaoFriendsDialog()
        }
    }

    private fun addObserver() {
        mBinding.viewModel?.let { memberManagementViewModel ->
            memberManagementViewModel.mShowLoadingView.observe(requireActivity(), Observer {
                when (it) {
                    true -> showLoadingView()
                    false -> hideLoadingView()
                }
            })

            memberManagementViewModel.gKakaoFriendList.observe(requireActivity(), Observer {
                val adapter = (mBinding.recyclerFriendList.adapter as? AddPeopleRecyclerViewAdapter)
                    ?: return@Observer
                adapter.setItemList(ArrayList(it))
                mBinding.groupTitle.txtSubTitle.text =
                    "${it.size}명 대기중..."
                memberManagementViewModel.updateLoadingFlag(false)
            })

            memberManagementViewModel.mSelectMember.observe(requireActivity(), Observer {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "Select Member : $it")
                it ?: return@Observer
            })
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onKeyDown(...) : keyCode : $keyCode, KeyEvent : ${event}")
        return false
    }

    private fun showAddKakaoFriendsDialog() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "showAddKakaoFriendsDialog(...)")
        val addFriendsFragment = AddFriendsDialogFragment.getInstance()
        when {
            addFriendsFragment.isAdded -> {
                return
            }
            else -> {
                addFriendsFragment.show(requireActivity().supportFragmentManager, tag)
            }
        }
    }
}