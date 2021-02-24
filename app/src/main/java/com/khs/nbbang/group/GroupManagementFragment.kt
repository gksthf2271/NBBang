package com.khs.nbbang.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.KeyboardUtils
import com.khs.nbbang.utils.setOnItemTouchListener
import com.khs.nbbang.utils.setTransitionListener
import kotlinx.android.synthetic.main.cview_page_title.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class GroupManagementFragment : BaseFragment() {
    lateinit var mBinding : FragmentGroupManagementBinding
    private val mItemTouchInterceptor = RecyclerViewTouchEvent()
    val mViewModel: MemberManagementViewModel by sharedViewModel()

    var mMemberList: ArrayList<Member> = arrayListOf(
        Member(0, "김한솔", 0, "월곡회", R.drawable.icon_user),
        Member(1, "신상은", 0, "월곡회", R.drawable.icon_user),
        Member(2, "정용인", 0, "월곡회", R.drawable.icon_user),
        Member(3, "김진혁", 0, "월곡회", R.drawable.icon_user),
        Member(4, "조현우", 0, "월곡회", R.drawable.icon_user),
        Member(5, "최종휘", 0, "월곡회", R.drawable.icon_user),
        Member(6, "김진근", 0, "월곡회", R.drawable.icon_user),
        Member(7, "이진형", 0, "월곡회", R.drawable.icon_user),
        Member(8, "배재룡", 0, "월곡회", R.drawable.icon_user),
        Member(9, "정준호", 0, "월곡회", R.drawable.icon_user),
        Member(10, "박소연", 0, "월곡회", R.drawable.icon_user),
        Member(11, "장선형", 0, "월곡회", R.drawable.icon_user),
        Member(12, "신주연", 0, "월곡회", R.drawable.icon_user),
        Member(13, "주경애", 0, "월곡회", R.drawable.icon_user)
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

        mBinding.viewModel.let {
            it!!.showMemberList()
            mBinding.addMemberView.setViewModel(it!!)
            mBinding.memberView.setViewModel(it!!)
        }

        mBinding.recyclerMemberList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(HistoryItemDecoration(30))
            addOnItemTouchListener(mItemTouchInterceptor)
            adapter =
                MemberRecyclerViewAdapter(arrayListOf()) {
                    Log.v(TAG, "ItemClicked : $it")
                    mBinding.viewModel!!.selectMember(it)
                    mBinding.motionLayout.setTransition(R.id.update_motion_transition)
                    mBinding.motionLayout.transitionToEnd()
                }
        }

        mBinding.btnAdd.setOnClickListener {
            mBinding.motionLayout.setTransition(R.id.add_motion_transition)
            mBinding.motionLayout.transitionToEnd()
        }

        mBinding.recyclerMemberList.setOnItemTouchListener {
            mBinding.motionLayout.setTransition(R.id.scroll_motion_transition)
        }

        mBinding.motionLayout.setTransitionListener({ start, end ->
            Log.v(TAG, "motionLayout Transition Changed: $start , end: $end")
        },{ start, end ->
            Log.v(TAG, "motionLayout State start: $start , end: $end")
            mItemTouchInterceptor.enable()
            KeyboardUtils().hideKeyboard(requireView(), requireContext())
        }, { completion ->
            mItemTouchInterceptor.disable()
            Log.v(TAG, "motionLayout State completion: $completion")
        })
    }

    private fun addObserver() {
        mBinding.viewModel ?: return

        mBinding.viewModel!!.mMemberList.observe(requireActivity(), Observer{
            val adapter = (mBinding.recyclerMemberList.adapter as? MemberRecyclerViewAdapter) ?: return@Observer
            adapter.setItem(it.nbbangMemberList)

            mBinding.groupTitle.txt_sub_title.text =
                "${(it?.nbbangMemberList ?: mMemberList).size}명 대기중..."
        })

        mBinding.viewModel!!.mSelectMember.observe(requireActivity(), Observer {
            Log.v(TAG,"Select Member : $it")
            it ?: return@Observer
            mBinding.memberView.setMember(it)
        })
    }
}