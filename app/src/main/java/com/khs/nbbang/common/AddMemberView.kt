package com.khs.nbbang.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.databinding.CviewAddMemberBinding
import com.khs.nbbang.group.MemberManagementViewModel
import com.khs.nbbang.page.ButtonCallBackListener
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.StringUtils
import kotlinx.android.synthetic.main.cview_title_description.view.*
import kotlinx.android.synthetic.main.cview_title_description.view.txt_title
import kotlinx.android.synthetic.main.cview_title_edittext.view.*
import org.koin.core.component.KoinComponent

class AddMemberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), KoinComponent{

    val TAG = this.javaClass.name
    var mBinding: CviewAddMemberBinding
    var gCurrentMember : Member
    var gCurrentMemberProfileUri : Uri? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewAddMemberBinding.inflate(inflater, this, true)
        gCurrentMember = Member()
    }

    fun initView() {
        mBinding.groupLayout.setOnClickListener { false }
        mBinding.let {
            GlideUtils().drawImageWithString(it.imgProfile, null, null)
            it.groupName.txt_title.text = "이름"
            it.groupDescription.txt_title.text = "분류"
        }
    }

    fun setCallBackListener(callback: ButtonCallBackListener) {
        mBinding.let {
            it.btnSave.setOnClickListener {
                gCurrentMember.name = mBinding.groupName.edit_description.text.toString()
                gCurrentMember.description = mBinding.groupDescription.edit_description.text.toString()
                gCurrentMember.profileUri = gCurrentMemberProfileUri
                Log.v(TAG,"save, profileUri : ${gCurrentMember.profileUri}")
                callback.onClickedSaveBtn(gCurrentMember)

                Toast.makeText(context, "멤버가 추가 되었습니다.", Toast.LENGTH_SHORT).show()
                clearView()
            }

            it.btnCancel.setOnClickListener {
                callback.onClickedCancelBtn()
                mBinding.groupName.edit_description.setText("")
                mBinding.groupDescription.edit_description.setText("")
            }

            it.imgProfile.setOnClickListener {
                Log.v(TAG,"Clicked ImgProfile!")
                callback.onClickedProfile(this)
            }
        }
    }

    fun clearView() {
        mBinding.groupName.edit_description.setText("")
        mBinding.groupDescription.edit_description.setText("")

        GlideUtils().drawImageWithT(mBinding.imgProfile, null, null)
    }

    fun setViewModel(vm : BaseViewModel) {
        mBinding.run {
            viewModel = vm as? MemberManagementViewModel
        }
    }

    fun updateProfileImg(photos: ArrayList<Uri>) {
        Log.v(TAG,"updateProfileImg(...)")
        GlideUtils().drawImageWithT(mBinding.imgProfile, photos.get(0), object :
            RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.v(TAG,"onLoadFailed, exception : $e")
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
                Log.v(TAG,"onResourceReady, resource : $resource")
                gCurrentMemberProfileUri = photos.get(0)
                return false
            }

        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }
}