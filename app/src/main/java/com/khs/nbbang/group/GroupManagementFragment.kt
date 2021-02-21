package com.khs.nbbang.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.nbbang.R
import com.khs.nbbang.animation.HistoryItemDecoration
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentGroupManagementBinding
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.adapter.AddPeopleRecyclerViewAdapter
import com.khs.nbbang.user.FreeUser
import com.khs.nbbang.user.User
import kotlinx.android.synthetic.main.cview_page_title.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class GroupManagementFragment : BaseFragment() {
    lateinit var mBinding : FragmentGroupManagementBinding
    lateinit var mRecyclerViewAdapter : MemberRecyclerViewAdapter
    val mViewModel: HistoryViewModel by sharedViewModel()
    var mUserList: List<User> = listOf(
        FreeUser(0, "김한솔", R.drawable.icon_user),
        FreeUser(1, "신상은", R.drawable.icon_user),
        FreeUser(2, "정용인", R.drawable.icon_user),
        FreeUser(3, "김진혁", R.drawable.icon_user),
        FreeUser(4, "조현우", R.drawable.icon_user),
        FreeUser(5, "최종휘", R.drawable.icon_user),
        FreeUser(6, "김진근", R.drawable.icon_user),
        FreeUser(7, "이진형", R.drawable.icon_user),
        FreeUser(8, "배재룡", R.drawable.icon_user),
        FreeUser(9, "정준호", R.drawable.icon_user),
        FreeUser(10, "박소연", R.drawable.icon_user),
        FreeUser(11, "장선형", R.drawable.icon_user),
        FreeUser(12, "신주연", R.drawable.icon_user),
        FreeUser(13, "주경애", R.drawable.icon_user)
    )

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
        initView()
        addObserver()
    }

    fun initView() {
        mBinding.groupTitle.txt_title.text = "Favorite Member"
        mBinding.groupTitle.txt_sub_title.text = "${mUserList.size}명 대기중..."
        mRecyclerViewAdapter = MemberRecyclerViewAdapter(requireContext(), mUserList) {
            Log.v(TAG,"ItemClicked : $it")
        }

        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            isFocusable = true
            addItemDecoration(HistoryItemDecoration(10))
            descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
            adapter = mRecyclerViewAdapter
        }
    }

    private fun addObserver() {
        mBinding.viewModel ?: return
    }
}