package com.khs.nbbang.localMember

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.common.FavoriteRecyclerAdapter
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.databinding.FragmentGroupManagementBinding
import com.khs.nbbang.page.FloatingButtonBaseFragment
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.user.Member
import kotlinx.android.synthetic.main.cview_page_title.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.lang.reflect.Array
import java.util.ArrayList

class GroupManagementFragment : FloatingButtonBaseFragment() {
    val mViewModel: MemberManagementViewModel by sharedViewModel()
    private val mGroupManagementContentsFragment by lazy { GroupManagementContentsFragment()
    }
    override fun makeContentsFragment(): Fragment? {
        return mGroupManagementContentsFragment
    }

    override fun init() {
        mGroupManagementContentsFragment.initView(this)
    }

    override fun add(obj: Member?) {
        obj?.let {
            if (obj !is Member) return
            mViewModel.let {
                it.saveMember(obj.id, obj.groupId, obj.name, obj.description, obj.kakaoId, obj.thumbnailImage, obj.profileImage, obj.profileUri) }
        }
    }

    override fun delete() {
        mViewModel.let {
            val selectMember = mViewModel.mSelectMember.value
            if (selectMember == null) {
                Log.e(TAG,"Delete Member Error!, selectMember is null!")
                return
            }
            it.deleteMember(selectMember)
        }
    }

    override fun update(member: Member) {
        Log.v(TAG,"update(...) member : $member")
        mViewModel.let {
            it.update(member)
        }
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (isShownMemberView()) {
                    hideAnyView()
                    return true
                }
            }
        }
        return false
    }

    companion object class GroupManagementContentsFragment : BaseFragment() {
        lateinit var mGroupManagementBinding: FragmentGroupManagementBinding
        val mViewModel: MemberManagementViewModel by sharedViewModel()
        private lateinit var mParentFragment: FloatingButtonBaseFragment

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_group_management, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            mGroupManagementBinding = DataBindingUtil.bind(view)!!
            mGroupManagementBinding.viewModel = mViewModel
        }

        fun initView(parentFragment: FloatingButtonBaseFragment) {
            mParentFragment = parentFragment
//            mGroupManagementBinding.toolbarTitle.title = "멤버 관리"

            // 갤러리 갔다가 다시 진입할 때 다시 그려지는 문제 발생 회피
            if (mGroupManagementBinding.recyclerMemberList.adapter == null) {
                mGroupManagementBinding.recyclerMemberList.apply {
                    layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                    addOnItemTouchListener(mParentFragment.mItemTouchInterceptor)
                    adapter =
                        AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                            Log.v(TAG, "ItemClicked : $it")
                            mGroupManagementBinding.viewModel!!.selectMember(it.second)
                            mParentFragment.showMemberView()
                        }
                }
                addObserver()
            }

            mGroupManagementBinding.viewModel?.let {
                it.showFavoriteMemberListByType(MemberType.TYPE_FREE_USER)
                mParentFragment.setViewModel(it)
            }
        }

        private fun addObserver() {
            mGroupManagementBinding.viewModel ?: return

            mGroupManagementBinding.viewModel!!.mShowLoadingView.observe(requireActivity(), Observer {
                when (it) {
                    true -> showLoadingView()
                    false -> hideLoadingView()
                }
            })

            mGroupManagementBinding.viewModel!!.mMemberList.observe(requireActivity(), Observer {
                val adapter = (mGroupManagementBinding.recyclerMemberList.adapter as? AddPeopleRecyclerViewAdapter)
                    ?: return@Observer
                adapter.setItemList(ArrayList(it))

                mGroupManagementBinding.groupTitle.txtTitle.text =
                    "Favorite Member"
                mGroupManagementBinding.groupTitle.txtSubTitle.text =
                    "${it.size}명 대기중..."
                mGroupManagementBinding.viewModel!!.updateLoadingFlag(false)
            })

            mGroupManagementBinding.viewModel!!.mSelectMember.observe(requireActivity(), Observer {
                Log.v(TAG, "Select Member : $it")
                it ?: return@Observer
                mParentFragment.selectMember(it)
            })
        }

        override fun onDestroyView() {
            super.onDestroyView()
            mGroupManagementBinding.recyclerMemberList.removeOnItemTouchListener(mParentFragment.mItemTouchInterceptor)
        }

        override fun makeCustomLoadingView(): Dialog? {
            Log.v(TAG,"makeCustomLoadingView(...)")
            return null
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
}