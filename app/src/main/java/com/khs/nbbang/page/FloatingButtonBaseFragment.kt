package com.khs.nbbang.page

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.khs.nbbang.R
import com.khs.nbbang.animation.RecyclerViewTouchEvent
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.common.AddMemberView
import com.khs.nbbang.common.MemberView
import com.khs.nbbang.databinding.FragmentFloatingBtnBaseBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.KeyboardUtils
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
        val v: View =
            inflater.inflate(R.layout.fragment_floating_btn_base, container, false)
        fillFragmentContainer(makeContentsFragment())
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        initView()
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    protected open fun fillFragmentContainer(fragment: Fragment?) {
        Log.d(TAG, "fill Fragment Container with fragment:$fragment")
        if (fragment != null && isAdded) {
            val ft =
                childFragmentManager.beginTransaction()
            ft.replace(R.id.contents_container, fragment)
            ft.commitNowAllowingStateLoss()
        } else {
            Log.d(TAG, "contentsFragment is null!!!")
        }
    }

    protected abstract fun makeContentsFragment(): Fragment?

    protected abstract fun init()

    protected abstract fun add(obj: Member?)

    protected abstract fun delete()

    protected abstract fun update(member: Member)

    private fun initView() {
        mBinding.memberView.setCallBackListener(this)
        mBinding.addMemberView.setCallBackListener(this)

        mBinding.btnAdd.setOnClickListener {
            showAddMemberView()
        }

        mBinding.motionLayout.setTransitionListener({ transitionName ->
            Log.v(TAG, "motionLayout Transition Changed: $transitionName")
            mItemTouchInterceptor.let { mItemTouchInterceptor.enable() }
        }, { start, end ->
            Log.v(TAG, "motionLayout State start: $start , end: $end")
            mItemTouchInterceptor.let { mItemTouchInterceptor.enable() }
            KeyboardUtils.hideKeyboard(requireView(), requireContext())
        }, { completion ->
            mItemTouchInterceptor.let { mItemTouchInterceptor.disable() }
            Log.v(TAG, "motionLayout State completion: $completion")
        })
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
        mBinding.let {
            updateTransition(R.id.add_motion_transition)
            mBinding.motionLayout.transitionToEnd()
        }
    }

    fun hideAddMemberView() {
        mBinding.let {
            updateTransition(R.id.add_motion_transition)
            mBinding.motionLayout.transitionToStart()
        }
    }

    fun selectMember(member: Member) {
        mBinding.let {
            mBinding.memberView.setMember(member)
        }
    }

    fun isShownMemberView() : Boolean {
        return (mBinding.motionLayout.transitionState as Bundle).getFloat("motion.progress") == 1.0f
    }

    override fun onClickedCancelBtn() {
        Log.v(
            TAG,
            "onClickedCancelBtn(...) transitionName : ${mBinding.motionLayout.transitionName}"
        )
        hideAnyView()
    }

    override fun onClickedDeleteBtn() {
        Log.v(TAG, "onClickedDeleteBtn(...)")
        hideMemeberView()
        delete()

    }

    override fun onClickedSaveBtn(obj: Member?) {
        Log.v(TAG, "onClickedSaveBtn(...)")
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
        Log.v(TAG, "Picked Images Url : ${StringUtils().listToAny(photos)}")
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