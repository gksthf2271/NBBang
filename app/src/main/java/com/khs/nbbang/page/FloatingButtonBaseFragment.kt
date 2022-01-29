package com.khs.nbbang.page

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khs.nbbang.R
import com.khs.nbbang.animation.RecyclerViewTouchEvent
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.common.AddMemberView
import com.khs.nbbang.common.MemberView
import com.khs.nbbang.databinding.FragmentFloatingBtnBaseBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.KeyboardUtils
import com.khs.nbbang.utils.LogUtil
import com.khs.nbbang.utils.StringUtils
import com.khs.nbbang.utils.setTransitionListener
import lv.chi.photopicker.PhotoPickerFragment

abstract class FloatingButtonBaseFragment : BaseFragment(), ButtonCallBackListener,
    PhotoPickerFragment.Callback {
    lateinit var mBinding: FragmentFloatingBtnBaseBinding
    var mItemTouchInterceptor = RecyclerViewTouchEvent()
    var mCurrentTransitionId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = FragmentFloatingBtnBaseBinding.inflate(inflater, container, true)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    protected abstract fun add(obj: Member?)

    protected abstract fun delete()

    protected abstract fun update(member: Member)

    private fun initView() {
        mBinding.apply {
            memberView.setCallBackListener(this@FloatingButtonBaseFragment)
            addMemberView.setCallBackListener(this@FloatingButtonBaseFragment)

            btnAdd.setOnClickListener {
                showAddMemberView()
            }

            motionLayout.setTransitionListener({ transitionName ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "motionLayout Transition Changed: $transitionName")
                mItemTouchInterceptor.run { mItemTouchInterceptor.enable() }
            }, { start, end ->
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "motionLayout State start: $start , end: $end")
                mItemTouchInterceptor.run { mItemTouchInterceptor.enable() }
                KeyboardUtils.hideKeyboard(requireView(), requireContext())
            }, { completion ->
                mItemTouchInterceptor.run { mItemTouchInterceptor.disable() }
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "motionLayout State completion: $completion")
            })
        }
    }

    private fun updateTransition(transitionId: Int) {
        mBinding.let {
            if (transitionId == 0) return
            mBinding.motionLayout.setTransition(transitionId)
            mCurrentTransitionId = transitionId
        }
    }

    fun setViewModel(vm: BaseViewModel) {
        mBinding.let {
            mBinding.addMemberView.setViewModel(vm)
            mBinding.memberView.setViewModel(vm)
        }
    }

    fun showMemberView() {
        mBinding.let {
            updateTransition(R.id.update_motion_transition)
            mBinding.motionLayout.transitionToEnd()
        }
    }

    fun hideAnyView() {
        if (isAdded) {
            mBinding.let {
                updateTransition(mCurrentTransitionId)
                mBinding.motionLayout.transitionToStart()
            }
        }
    }

    fun hideMemeberView() {
        mBinding.let {
            updateTransition(R.id.update_motion_transition)
            mBinding.motionLayout.transitionToStart()
        }
    }

    fun showAddMemberView() {
        mBinding.run {
            updateTransition(R.id.add_motion_transition)
            mBinding.motionLayout.transitionToEnd()
        }
    }

    fun hideAddMemberView() {
        mBinding.run {
            updateTransition(R.id.add_motion_transition)
            motionLayout.transitionToStart()
        }
    }

    fun selectMember(member: Member) {
        mBinding.run {
            memberView.setMember(member)
        }
    }

    fun isShownMemberView() : Boolean {
        return (mBinding.motionLayout.transitionState as Bundle).getFloat("motion.progress") == 1.0f
    }

    override fun onClickedCancelBtn() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onClickedCancelBtn(...) transitionName : ${mBinding.motionLayout.transitionName}")
        hideAnyView()
    }

    override fun onClickedDeleteBtn() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onClickedDeleteBtn(...)")
        hideMemeberView()
        delete()

    }

    override fun onClickedSaveBtn(obj: Member?) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onClickedSaveBtn(...)")
        hideAddMemberView()
        add(obj)
    }

    override fun onClickedUpdateBtn(member: Member) {
        hideMemeberView()
        update(member)
    }

    override fun onClickedProfile(view: View) {
        gCurrentView = view
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = false,
            maxSelection = 1,
            theme = R.style.PhotoPickerStyle
        ).show(childFragmentManager, null)
    }

    var gCurrentView : View? = null

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "Picked Images Url : ${StringUtils().listToAny(photos)}")
        when (gCurrentView) {
            is MemberView -> {
                mBinding.memberView.updateProfileImg(photos)
            }
            is AddMemberView -> {
                mBinding.addMemberView.updateProfileImg(photos)
            }
        }
    }
}

interface ButtonCallBackListener {
    fun onClickedSaveBtn(obj: Member?)
    fun onClickedCancelBtn()
    fun onClickedDeleteBtn()
    fun onClickedUpdateBtn(obj: Member)
    fun onClickedProfile(view: View)
}