package com.khs.nbbang.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Observer
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.freeUser.FreeUserActivity
import com.khs.nbbang.kakaoUser.KakaoUserActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    //Koin을 이용한 ViewModel 주입
    val mLoginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initObserver()
        initView()
    }

    fun initObserver() {
        mLoginViewModel.mLoginCookie.observe(this, Observer{
            Log.d(TAG,"Login Cookie >>> ${it.cookieData}")
            if (!TextUtils.isEmpty(it.cookieData)) {
                launch<KakaoUserActivity>(startForResult, null)
            }
        })

        mLoginViewModel.mIsLogin.observe(this, Observer {
            when (it) {
                true -> launch<KakaoUserActivity>(startForResult, null)
                false -> launch<FreeUserActivity>(startForResult, null)
            }
        })
    }

    fun initView() {
        btn_kakao_login.setOnClickListener {
            Log.v(TAG,"btn_kakao_login clicked(...)")
            mLoginViewModel.login()
        }

        btn_free.setOnClickListener {
            Log.v(TAG,"btn_free clicked(...)")
            launch<FreeUserActivity>(startForResult, null)
        }
    }
}