package com.khs.nbbang.group

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.findFragment
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewMemberBinding
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_memeber_item.view.*
import kotlinx.android.synthetic.main.cview_title_edittext.view.*
import org.koin.core.component.KoinComponent

class MemberView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent {

    val TAG = this.javaClass.name
    var mBinding: CviewMemberBinding

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
        mBinding.let {
            GlideUtils().drawImageWithResId(it.imgProfile, member.resId, null)
            it.groupName.txt_description.text = member.name
            it.groupDescription.txt_description.text = member.description

            it.btnDelete.setOnClickListener {
                mBinding.viewModel.let {
                    it!!.deleteMember(member)
                    findFragment<GroupManagementFragment>().mBinding.motionLayout.transitionToStart()
                }
            }

            it.btnCancel.setOnClickListener {
                findFragment<GroupManagementFragment>().mBinding.motionLayout.transitionToStart()
            }
        }
    }

    fun setViewModel(vm : BaseViewModel) {
        mBinding.run {
            viewModel = vm as? MemberManagementViewModel
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }
}