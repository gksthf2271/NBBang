package com.khs.nbbang.mypage

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentMyPageBinding
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_title_description.view.*
import kotlinx.android.synthetic.main.cview_title_description.view.txt_title
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MyPageFragment : BaseFragment() {
    lateinit var mBinding : FragmentMyPageBinding
    val mViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = DataBindingUtil.bind(view)!!
        mBinding.viewModel = mViewModel
        mBinding.fragment = this
        initView()
        addObserver()
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG,"makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        updateProfileInfo(null, null)
    }

    private fun addObserver() {
        mBinding.viewModel ?: return

        mBinding.viewModel!!.gMyData.observe(requireActivity(), Observer {
            if (it != null) {
                Log.v(TAG, "mMyDataFrom : ${it}")
                if (it != null) {
                    val id = it.id
                    val name = it.properties?.get("nickname")
                    val image = it.properties?.get("profile_image")
                    val thumbnail = it.properties?.get("thumbnail_image")
                    Log.v(
                        TAG, "MyData id : ${id}"
                                + "\n name : ${name}"
                                + "\n profile_image : ${image}"
                                + "\n thumbnail_image : ${thumbnail}"
                    )
                    updateProfileInfo(thumbnail, name)
                } else {
                    updateProfileInfo(null, null)
                }

            } else {
                Log.v(TAG, "isLogin : $it")
                updateProfileInfo(null, null)
            }
        })
    }


    private fun updateProfileInfo(thumbnail: String?, name: String?) {
        mBinding.viewModel.let {
            GlideUtils().drawImageWithString(mBinding.imgProfile, thumbnail, null)
            mBinding.groupName.txt_title.text = "이름"
            mBinding.groupName.txt_description.text = name ?: "FREE USER"

            if (name != null && it!!.gIsLogin.value == true) {
                mBinding.txtLogout.visibility = View.VISIBLE
                mBinding.btnLoginLogout.visibility = View.INVISIBLE
            } else {
                mBinding.btnLoginLogout.visibility = View.VISIBLE
                mBinding.txtLogout.visibility = View.INVISIBLE
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                //todo 팝업 기능 추가 시 hide는 여기서 처리
            }
        }
        return false
    }
}