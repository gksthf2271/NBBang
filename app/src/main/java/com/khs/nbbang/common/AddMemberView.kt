package com.khs.nbbang.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewAddMemberBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.page.ButtonCallBackListener
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.LogUtil
import kotlinx.android.synthetic.main.cview_title_edittext.view.*
import org.koin.core.component.KoinComponent

class AddMemberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent{

    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_UI
    var mBinding: CviewAddMemberBinding
    var gCurrentMember : Member
    var gCurrentMemberProfileUri : String? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewAddMemberBinding.inflate(inflater, this, true)
        gCurrentMember = Member()
    }

    fun initView() {
        mBinding.groupLayout.setOnClickListener { false }
        mBinding.let {
            GlideUtils().drawImageWithString(it.imgProfile, null, null)
            it.groupName.txtTitle.text = "이름"
            it.groupDescription.txtTitle.text = "분류"
        }
    }

    fun setCallBackListener(callback: ButtonCallBackListener) {
        mBinding.let {
            it.btnSave.setOnClickListener {
                val inputName = mBinding.groupName.editDescription.text.toString()
                if (inputName.replace(" ", "").isNullOrEmpty()) {
                    Toast.makeText(context, "이름을 입력해주세요!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                gCurrentMember.name = mBinding.groupName.editDescription.text.toString()
                gCurrentMember.description = mBinding.groupDescription.editDescription.text.toString()
                gCurrentMember.profileUri = gCurrentMemberProfileUri
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "save, profileUri : ${gCurrentMember.profileUri}")
                callback.onClickedSaveBtn(gCurrentMember)

                clearView()
            }

            it.btnCancel.setOnClickListener {
                callback.onClickedCancelBtn()
                clearView()
            }

            it.imgProfile.setOnClickListener {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked ImgProfile!")
                callback.onClickedProfile(this)
            }
        }
    }

    fun clearView() {
        gCurrentMember = Member()
        mBinding.groupName.editDescription.setText("")
        mBinding.groupDescription.editDescription.setText("")

        GlideUtils().drawImageWithT(mBinding.imgProfile, null, null)
    }

    fun setViewModel(vm : BaseViewModel) {
        mBinding.run {
            viewModel = vm as? MemberManagementViewModel
        }
    }

    fun updateProfileImg(photos: ArrayList<Uri>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "updateProfileImg(...)")
        GlideUtils().drawImageWithT(mBinding.imgProfile, photos.get(0), object :
            RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onLoadFailed, exception : $e")
                gCurrentMemberProfileUri = null
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onResourceReady, resource : $resource")
                gCurrentMemberProfileUri = photos.get(0).toString()
                return false
            }

        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }
}