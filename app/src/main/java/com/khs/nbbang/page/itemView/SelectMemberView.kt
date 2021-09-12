package com.khs.nbbang.page.itemView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.MainActivity
import com.khs.nbbang.R
import com.khs.nbbang.databinding.CviewTextPeopleBinding
import com.khs.nbbang.kakaoFriends.AddFriendsDialogFragment
import com.khs.nbbang.kakaoFriends.KakaoFavoriteFriendsFragment
import com.khs.nbbang.page.viewModel.SelectMemberViewModel
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DisplayUtils
import com.khs.nbbang.utils.GlideUtils

class SelectMemberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val mBinding: CviewTextPeopleBinding
    private val gClickLock : Any = Any()
    val TAG = this.javaClass.simpleName
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
            Log.v(TAG,"memberName : ${member.name}, isSelected : $gIsSelectedView")
            mBinding.txtName.text = member.name
            GlideUtils().drawMemberProfile(mBinding.imgProfile, member, null)
            mBinding.layoutRoot.setOnClickListener {
                Log.v(TAG,"onClicked Member! before isSelected : $gIsSelectedView ----------------------------")
                gIsSelectedView = !gIsSelectedView
                selectCircleView(gIsSelectedView, member, callback)
                Log.v(TAG,"------------------after isSelected : $gIsSelectedView ----------------------------")
            }
            selectCircleView(gIsSelectedView, member, callback)
        }
    }

    private fun selectCircleView(isSelected: Boolean, member: Member, callback : (Boolean, Member) -> Unit) {
        synchronized(gClickLock) {
            Log.v(TAG, "selectCircleView(...) isSelected : $isSelected")

            mBinding.txtName.isSelected = isSelected
            mBinding.layoutProfile.isSelected = isSelected

            mBinding.layoutRoot.setTransition(R.id.transition_update_circle_background)
            when (isSelected) {
                true -> {
                    Log.v(TAG, "motion transition To End!")
                    mBinding.layoutRoot.transitionToEnd()
                    callback(true, member)
                }
                false -> {
                    Log.v(TAG, "motion transition To Start!")
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
        Log.v(TAG,"setCheckedMember(...) isSelected : $isSelected")
        gIsSelectedView = isSelected
    }
}