package com.khs.nbbang.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    lateinit var mBinding: ActivityLoginBinding
    //Koin을 이용한 ViewModel 주입
    val mLoginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        mBinding.viewModel = mLoginViewModel
//        initObserver()
    }

//    fun initObserver() {
//        mBinding.viewModel.let {
//            it!!.mLoginCookie.observe(this, Observer {
//                Log.d(TAG, "Login Cookie >>> ${it.cookieData}")
//                if (!TextUtils.isEmpty(it.cookieData)) {
//                    loadKakaoUserActivity()
//                }
//            })
//
//            it!!.mIsLogin.observe(this, Observer {
//                when (it) {
//                    true -> loadKakaoUserActivity()
//                    false -> loadMainActivity()
//                }
//            })
//        }
//    }

//    fun loadMainActivity() {
//        launch<MainActivity>(startForResult, null)
//    }

//    fun loadFreeUserActivity(){
//        launch<FreeUserActivity>(startForResult, null)
//    }
//
//    fun loadKakaoUserActivity() {
//        launch<KakaoUserActivity>(startForResult, null)
//    }
}