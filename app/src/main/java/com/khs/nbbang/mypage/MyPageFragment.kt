package com.khs.nbbang.mypage

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.khs.nbbang.base.BaseFragment
import com.khs.nbbang.databinding.FragmentMyPageBinding
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.utils.GlideUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MyPageFragment : BaseFragment() {
    lateinit var mBinding : FragmentMyPageBinding
    val mViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMyPageBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = mViewModel
        mBinding.fragment = this
        initView()
        addObserver()
    }

    override fun makeCustomLoadingView(): Dialog? {
        Log.v(TAG_CLASS,"makeCustomLoadingView(...)")
        return null
    }

    fun initView() {
        updateProfileInfo(null, null)
    }

    private fun addObserver() {
        mBinding.viewModel ?: return
        mBinding.viewModel?.let { loginViewModel ->
            loginViewModel.gMyData.observe(requireActivity(), Observer {
                if (it != null) {
                    Log.v(TAG_CLASS, "mMyDataFrom : ${it}")
                    if (it != null) {
                        val id = it.id
                        val name = it.properties?.get("nickname")
                        val image = it.properties?.get("profile_image")
                        val thumbnail = it.properties?.get("thumbnail_image")
                        Log.v(
                            TAG_CLASS, "MyData id : ${id}"
                                    + "\n name : ${name}"
                                    + "\n profile_image : ${image}"
                                    + "\n thumbnail_image : ${thumbnail}"
                        )
                        updateProfileInfo(thumbnail, name)
                    } else {
                        updateProfileInfo(null, null)
                    }

                } else {
                    Log.v(TAG_CLASS, "isLogin : $it")
                    updateProfileInfo(null, null)
                }
            })
        }
    }


    private fun updateProfileInfo(thumbnail: String?, name: String?) {
        mBinding.viewModel?.let {
            GlideUtils().drawImageWithString(mBinding.imgProfile, thumbnail, null)
            mBinding.groupName.txtTitle.text = "이름"
            mBinding.groupName.txtDescription.text = name ?: "FREE USER"

            if (name != null && it.gIsLogin.value == true) {
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