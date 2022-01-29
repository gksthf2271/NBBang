package com.khs.nbbang.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewMemberBinding
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.page.ButtonCallBackListener
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.LogUtil
import org.koin.core.component.KoinComponent

class MemberView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent {

    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_UI
    var mBinding: CviewMemberBinding
    lateinit var gCurrentMember : Member
    var gCurrentImageUri = ""
    private val URI_HEADER = "android.resource://com.khs.nbbang/"

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewMemberBinding.inflate(inflater, this,true)
    }

    fun initView() {
        mBinding.groupLayout.setOnClickListener { false }
        mBinding.let {
            GlideUtils().drawImageWithString(it.imgProfile, null, null)
            it.groupName.txtTitle.text = "이름"
            it.groupDescription.txtTitle.text = "분류"
        }
    }

    fun updateProfileImg(photos: ArrayList<Uri>) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "updateProfileImg(...)")
        GlideUtils().drawImageWithT(mBinding.imgProfile, photos.get(0), object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onLoadFailed, exception : $e")
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "onResourceReady, resource : $resource")
                gCurrentImageUri = photos.get(0).toString()
                return false
            }

        })
    }

    fun setMember(member: Member) {
        gCurrentMember = member
        mBinding.apply {
            GlideUtils().drawMemberProfile(imgProfile, member, null)
            groupName.editDescription.setText(gCurrentMember.name)
            groupDescription.editDescription.setText(gCurrentMember.description)
            groupDescription.root.visibility = View.VISIBLE
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
        mBinding.apply {
            btnDelete.setOnClickListener {
                gCurrentImageUri = ""
                callback.onClickedDeleteBtn()
            }

            btnCancel.setOnClickListener {
                gCurrentImageUri = ""
                callback.onClickedCancelBtn()
            }

            btnUpdate.setOnClickListener {
                gCurrentMember.name = mBinding.groupName.editDescription.text.toString()
                gCurrentMember.description = mBinding.groupDescription.editDescription.text.toString()
                callback.onClickedUpdateBtn(gCurrentMember.apply {
                    if (!gCurrentImageUri.isNullOrEmpty()) {
                        profileUri = gCurrentImageUri
                    }
                })
            }

            imgProfile.setOnClickListener {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "Clicked ImgProfile!")
                callback.onClickedProfile(root)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }
}