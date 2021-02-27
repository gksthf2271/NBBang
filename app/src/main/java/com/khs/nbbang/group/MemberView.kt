package com.khs.nbbang.group

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewMemberBinding
import com.khs.nbbang.page.ButtonCallBackListener
import com.khs.nbbang.page.ItemObj.JoinPeople
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.user.Member
import com.khs.nbbang.user.User
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_title_description.view.*
import org.koin.core.component.KoinComponent

class MemberView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent {

    val TAG = this.javaClass.name
    var mBinding: CviewMemberBinding
    lateinit var mCurrentUser : People

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewMemberBinding.inflate(inflater, this,true)
    }

    fun initView() {
        mBinding.let {
            GlideUtils().drawImage(it.imgProfile, null, null)
            it.groupName.txt_title.text = "이름"
            it.groupDescription.txt_title.text = "분류"
        }
    }

    fun setMember(member: Member) {
        mCurrentUser = member
        mBinding.let {
            GlideUtils().drawImageWithResId(it.imgProfile, member.resId, null)
            it.groupName.txt_description.text = member.name
            it.groupDescription.txt_description.text = member.description
            it.groupDescription.visibility = View.VISIBLE
        }
    }

    fun setPeople(joinPeople: JoinPeople) {
        mCurrentUser = joinPeople
        mBinding.let {
            GlideUtils().drawImageWithResId(it.imgProfile, joinPeople.resId, null)
            it.groupName.txt_description.text = joinPeople.name
            it.groupDescription.visibility = View.INVISIBLE
        }
    }

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
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }
}