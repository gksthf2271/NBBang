package com.khs.nbbang.page.itemView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.databinding.CviewTextPeopleBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DisplayUtils
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.LogUtil

class SelectMemberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val mBinding: CviewTextPeopleBinding
    private val gClickLock : Any = Any()
    private val TAG_CLASS = this.javaClass.simpleName
    private val LOG_TAG = LogUtil.TAG_UI
    private var gIsSelectedView = false

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewTextPeopleBinding.inflate(inflater, this, true)
    }

    fun setViewSize(horizontalRowCount: Int) {
        val viewSize = DisplayUtils().getItemViewSize(context, horizontalRowCount)
        layoutParams = LayoutParams(viewSize, viewSize)
    }

    fun setMember(member: Member, callback : (Boolean, Member) -> Unit) {
        tag = member
        mBinding.let {
            LogUtil.vLog(LOG_TAG, TAG_CLASS, "memberName : ${member.name}, isSelected : $gIsSelectedView")
            mBinding.txtName.text = member.name
            GlideUtils().drawMemberProfile(mBinding.imgProfile, member, null)
            mBinding.layoutRoot.setOnClickListener {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onClicked Member! before isSelected : $gIsSelectedView ----------------------------")
                gIsSelectedView = !gIsSelectedView
                selectCircleView(gIsSelectedView, member, callback)
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "------------------after isSelected : $gIsSelectedView ----------------------------")
            }
            selectCircleView(gIsSelectedView, member, callback)
        }
    }

    private fun selectCircleView(isSelected: Boolean, member: Member, callback : (Boolean, Member) -> Unit) {
        synchronized(gClickLock) {
            LogUtil.vLog(LOG_TAG, TAG_CLASS, "selectCircleView(...) isSelected : $isSelected")

            mBinding.txtName.isSelected = isSelected
            mBinding.layoutProfile.isSelected = isSelected

            mBinding.layoutRoot.setTransition(R.id.transition_update_circle_background)
            when (isSelected) {
                true -> {
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "motion transition To End!")
                    mBinding.layoutRoot.transitionToEnd()
                    callback(true, member)
                }
                false -> {
                    LogUtil.vLog(LOG_TAG, TAG_CLASS, "motion transition To Start!")
                    mBinding.layoutRoot.transitionToStart()
                    callback(false, member)
                }
            }
        }
    }

    fun isCheckedMember() : Boolean {
        return gIsSelectedView
    }

    fun setCheckedMember(isSelected: Boolean) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setCheckedMember(...) isSelected : $isSelected")
        gIsSelectedView = isSelected
    }
}