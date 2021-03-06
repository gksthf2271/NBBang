package com.khs.nbbang.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewMemberBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.page.ButtonCallBackListener
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_title_description.view.*
import kotlinx.android.synthetic.main.cview_title_description.view.txt_title
import kotlinx.android.synthetic.main.cview_title_edittext.view.*
import lv.chi.photopicker.PhotoPickerFragment
import org.koin.core.component.KoinComponent

class MemberView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent {

    val TAG = this.javaClass.name
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
            it.groupName.txt_title.text = "이름"
            it.groupDescription.txt_title.text = "분류"
        }
    }

    fun updateProfileImg(photos: ArrayList<Uri>) {
        Log.v(TAG,"updateProfileImg(...)")
        GlideUtils().drawImageWithT(mBinding.imgProfile, photos.get(0), object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.v(TAG,"onLoadFailed, exception : $e")
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                Log.v(TAG,"onResourceReady, resource : $resource")
                gCurrentImageUri = photos.get(0).toString()
                return false
            }

        })
    }

    fun setMember(member: Member) {
        gCurrentMember = member
        mBinding.let {
            GlideUtils().drawMemberProfile(it.imgProfile, member, null)
            it.groupName.edit_description.setText(gCurrentMember.name)
            it.groupDescription.edit_description.setText(gCurrentMember.description)
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
                gCurrentImageUri = ""
                callback.onClickedDeleteBtn()
            }

            it.btnCancel.setOnClickListener {
                gCurrentImageUri = ""
                callback.onClickedCancelBtn()
            }

            it.btnUpdate.setOnClickListener {
                gCurrentMember.name = mBinding.groupName.edit_description.text.toString()
                gCurrentMember.description = mBinding.groupDescription.edit_description.text.toString()
                callback.onClickedUpdateBtn(gCurrentMember.apply {
                    if (!gCurrentImageUri.isNullOrEmpty()) {
                        profileUri = gCurrentImageUri
                    }
                })
            }

            it.imgProfile.setOnClickListener {
                Log.v(TAG,"Clicked ImgProfile!")
                callback.onClickedProfile(this)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }
}