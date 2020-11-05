package com.khs.nbbang.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Observer
import com.khs.nbbang.MainActivity
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    //Koin을 이용한 ViewModel 주입
    val mLoginViewModel: LoginViewModel by viewModel()
    val KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE"

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
                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra(KEY_LOGIN_TYPE, LoginType.TYPE_KAKAO)
                startActivity(intent)
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
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra(KEY_LOGIN_TYPE, LoginType.TYPE_FREE)
            startActivity(intent)
        }
    }
}