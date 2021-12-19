package com.khs.nbbang.localMember

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.databinding.FragmentGroupManagementBinding
import com.khs.nbbang.page.FloatingButtonBaseFragment
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.user.Member
import kotlinx.android.synthetic.main.cview_page_title.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class GroupManagementFragment : FloatingButtonBaseFragment() {
    lateinit var mFragmentGroupManagementBinding: FragmentGroupManagementBinding
    val mViewModel: MemberManagementViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentGroupManagementBinding = FragmentGroupManagementBinding.inflate(inflater, container, false)
        super.onCreateView(inflater, mFragmentGroupManagementBinding.root as ViewGroup, savedInstanceState)
        return mFragmentGroupManagementBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentGroupManagementBinding.viewModel = mViewModel
    }

    override fun onStart() {
        super.onStart()
        initView()
        addObserver()
    }

    private fun initView() {
//            mGroupManagementBinding.toolbarTitle.title = "멤버 관리"
        mFragmentGroupManagementBinding.viewModel?.let { memberManagementViewModel ->
            // 갤러리 갔다가 다시 진입할 때 다시 그려지는 문제 발생 회피
            if (mFragmentGroupManagementBinding.recyclerMemberList.adapter == null) {
                mFragmentGroupManagementBinding.recyclerMemberList.apply {
                    layoutManager =
                        GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                    addOnItemTouchListener(mItemTouchInterceptor)
                    adapter =
                        AddPeopleRecyclerViewAdapter(requireContext(), arrayListOf()) {
                            Log.v(TAG, "ItemClicked : $it")
                            memberManagementViewModel.selectMember(it.second)
                            showMemberView()
                        }
                }
                addObserver()
            }

            memberManagementViewModel.showFavoriteMemberListByType(MemberType.TYPE_FREE_USER)
            setViewModel(memberManagementViewModel)
        }
    }

    private fun addObserver() {
        mFragmentGroupManagementBinding.viewModel?.let { memberManagementViewModel ->
            memberManagementViewModel.mShowLoadingView.observe(requireActivity(), Observer {
                when (it) {
                    true -> showLoadingView()
                    false -> hideLoadingView()
                }
            })

            memberManagementViewModel.mMemberList.observe(requireActivity(), Observer {
                val adapter = (mFragmentGroupManagementBinding.recyclerMemberList.adapter as? AddPeopleRecyclerViewAdapter)
                    ?: return@Observer
                adapter.setItemList(ArrayList(it))

                mFragmentGroupManagementBinding.groupTitle.txtTitle.text =
                    "Favorite Member"
                mFragmentGroupManagementBinding.groupTitle.txtSubTitle.text =
                    "${it.size}명 대기중..."
                memberManagementViewModel.updateLoadingFlag(false)
            })

            memberManagementViewModel.mSelectMember.observe(requireActivity(), Observer {
                Log.v(TAG, "Select Member : $it")
                it ?: return@Observer
                selectMember(it)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFragmentGroupManagementBinding.recyclerMemberList.removeOnItemTouchListener(mItemTouchInterceptor)
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
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
}