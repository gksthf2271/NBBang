package com.khs.nbbang

import android.content.Intent
import android.os.Bundle
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.freeUser.FreeUserActivity
import com.khs.nbbang.login.LoginActivity
import com.khs.nbbang.login.LoginCookie
import com.khs.nbbang.login.LoginType
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    val loginCookieInject by inject<LoginCookie>()
    val REQUEST_FREE_USER = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    fun initView() {
        val loginType = intent.getSerializableExtra(LoginActivity().KEY_LOGIN_TYPE)

        when(loginType) {
            LoginType.TYPE_FREE -> {
                val intent = Intent(this, FreeUserActivity::class.java)
                startActivityForResult(intent, REQUEST_FREE_USER)
            }
            LoginType.TYPE_KAKAO -> {

            }
        }
    }

    fun checkLogin() {
        loginCookieInject.cookieData
    }
}