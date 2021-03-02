package com.khs.nbbang.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewMemberBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.page.ButtonCallBackListener
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_title_edittext.view.*
import org.koin.core.component.KoinComponent

class MemberView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent {

    val TAG = this.javaClass.name
    var mBinding: CviewMemberBinding
    lateinit var mCurrentMember : Member

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewMemberBinding.inflate(inflater, this,true)
    }

    fun initView() {
        mBinding.groupLayout.setOnClickListener { false }
        mBinding.let {
            GlideUtils().drawImage(it.imgProfile, null, null)
            it.groupName.txt_title.text = "이름"
            it.groupDescription.txt_title.text = "분류"
        }
    }

    fun setMember(member: Member) {
        mCurrentMember = member
        mBinding.let {
            GlideUtils().drawImageWithResId(it.imgProfile, member.resId, null)
            it.groupName.edit_description.setText(member.name)
            it.groupDescription.edit_description.setText(member.description)
            it.groupDescription.visibility = View.VISIBLE
        }
    }

//    fun setPeople(joinPeople: JoinPeople) {
//        mCurrentUser = joinPeople
//        mBinding.let {
//            GlideUtils().drawImageWithResId(it.imgProfile, joinPeople.resId, null)
//            it.groupName.edit_description.setText(joinPeople.name)
//            it.groupDescription.visibility = View.INVISIBLE
//        }
//    }

    fun setViewModel(vm : BaseViewModel) {
        mBinding.run {
            viewModel = vm as? MemberManagementViewModel
        }
    }

    fun setCallBackListener(callback: ButtonCallBackListener) {
        mBinding.let {
            it.btnDelete.setOnClickListener {
                callback.onClickedDeleteBtn()
            }

            it.btnCancel.setOnClickListener {
                callback.onClickedCancelBtn()
            }

            it.btnUpdate.setOnClickListener {
                callback.onClickedUpdateBtn(
                    mBinding.groupName.edit_description.text.toString(),
                    mBinding.groupDescription.edit_description.text.toString(),
                    R.drawable.icon_user
                )
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }
}