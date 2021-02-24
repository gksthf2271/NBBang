package com.khs.nbbang.group

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.findFragment
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewAddMemberBinding
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_title_edittext.view.*
import org.koin.core.component.KoinComponent

class AddMemberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent {

    val TAG = this.javaClass.name
    var mBinding: CviewAddMemberBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewAddMemberBinding.inflate(inflater, this,true)
    }

    fun initView() {
        mBinding.let {
            GlideUtils().drawImage(it.imgProfile, null, null)
            it.groupName.txt_title.text = "이름"
            it.groupDescription.txt_title.text = "분류"

            it.btnSave.setOnClickListener {
                mBinding.viewModel.let {
                    it!!.saveMember(
                        null, null, mBinding.groupName.edit_description.text.toString(),
                        mBinding.groupDescription.edit_description.text.toString(), null
                    )
                    mBinding.groupName.edit_description.setText("")
                    mBinding.groupDescription.edit_description.setText("")
                    Toast.makeText(context,"멤버가 추가 되었습니다.", Toast.LENGTH_SHORT).show()
                    findFragment<GroupManagementFragment>().mBinding.motionLayout.transitionToStart()
                    mBinding.groupName.edit_description.isSelected = true
                }
            }

            it.btnCancel.setOnClickListener {
                mBinding.groupName.edit_description.setText("")
                mBinding.groupDescription.edit_description.setText("")
                findFragment<GroupManagementFragment>().mBinding.motionLayout.transitionToStart()
                mBinding.groupName.edit_description.isSelected = true
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