package com.khs.nbbang.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.animation.RecyclerViewTouchEvent
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentGroupManagementBinding
import com.khs.nbbang.page.FloatingButtonBaseFragment
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.dutchPayPageFragments.AddPeopleFragment
import com.khs.nbbang.user.Member
import com.khs.nbbang.user.User
import com.khs.nbbang.utils.KeyboardUtils
import com.khs.nbbang.utils.setOnItemTouchListener
import com.khs.nbbang.utils.setOnScorllingListenenr
import com.khs.nbbang.utils.setTransitionListener
import kotlinx.android.synthetic.main.cview_page_title.view.*
import kotlinx.android.synthetic.main.cview_page_title.view.txt_title
import kotlinx.android.synthetic.main.cview_title_edittext.view.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.sharedViewModel

class GroupManagementFragment : FloatingButtonBaseFragment() {
    val mViewModel: MemberManagementViewModel by sharedViewModel()
    val mGroupManagementContentsFragment by lazy { GroupManagementContentsFragment()
    }
    override fun makeContentsFragment(): Fragment? {
        return mGroupManagementContentsFragment
    }

    override fun init() {
        mGroupManagementContentsFragment.initView(this)
    }

    override fun add(obj: People?) {
        obj.let {
            if (obj !is Member) return
            mViewModel.let {
                it!!.saveMember(obj.id, obj.groupId, obj.name, obj.description, obj.resId) }
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

    override fun update(old: People, new: People) {
        Log.v(TAG,"update(...)")
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