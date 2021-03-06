package com.khs.nbbang.group

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentGroupManagementBinding
import com.khs.nbbang.page.FloatingButtonBaseFragment
import com.khs.nbbang.user.Member
import kotlinx.android.synthetic.main.cview_page_title.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

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
        obj.let {
            if (obj !is Member) return
            mViewModel.let {
                it!!.saveMember(obj.id, obj.groupId, obj.name, obj.description, obj.kakaoId, obj.thumbnailImage, obj.profileImage, obj.profileUri) }
        }
    }

    override fun delete() {
        mViewModel.let {
            val selectMember = mViewModel.mSelectMember.value
            if (selectMember == null) {
                Log.e(TAG,"Delete Member Error!, selectMember is null!")
                return
            }
            it!!.deleteMember(selectMember)
        }
    }

    override fun update(member: Member) {
        Log.v(TAG,"update(...) member : $member")
        mViewModel.let {
            it!!.update(member)
        }
    }

    companion object class GroupManagementContentsFragment : BaseFragment() {
        lateinit var mBinding: FragmentGroupManagementBinding
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
            mBinding = DataBindingUtil.bind(view)!!
            mBinding.viewModel = mViewModel
        }

        fun initView(parentFragment: FloatingButtonBaseFragment) {
            mParentFragment = parentFragment
            mBinding.groupTitle.txt_title.text = "Favorite Member"

            mBinding.viewModel.let {
                it!!.showMemberList()
                mParentFragment.setViewModel(it!!)
            }

            // 갤러리 갔다가 다시 진입할 때 다시 그려지는 문제 발생 회피
            if (mBinding.recyclerMemberList.adapter == null) {
                mBinding.recyclerMemberList.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    addItemDecoration(HistoryItemDecoration(30))
                    addOnItemTouchListener(mParentFragment.mItemTouchInterceptor)
                    adapter =
                        MemberRecyclerViewAdapter(arrayListOf()) {
                            Log.v(TAG, "ItemClicked : $it")
                            mBinding.viewModel!!.selectMember(it)
                            mParentFragment.showMemberView()
                        }
                }
                addObserver()
            }
        }

        private fun addObserver() {
            mBinding.viewModel ?: return

            mBinding.viewModel!!.mMemberList.observe(requireActivity(), Observer {
                val adapter = (mBinding.recyclerMemberList.adapter as? MemberRecyclerViewAdapter)
                    ?: return@Observer
                adapter.setItem(it)

                mBinding.groupTitle.txt_sub_title.text =
                    "${it.size}명 대기중..."
            })

            mBinding.viewModel!!.mSelectMember.observe(requireActivity(), Observer {
                Log.v(TAG, "Select Member : $it")
                it ?: return@Observer
                mParentFragment.selectMember(it)
            })
        }

        override fun onDestroyView() {
            super.onDestroyView()
            mBinding.recyclerMemberList.removeOnItemTouchListener(mParentFragment.mItemTouchInterceptor)
        }
    }
}