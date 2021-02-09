package com.khs.nbbang

import android.os.Bundle
import com.khs.nbbang.base.BaseActivity
import com.khs.nbbang.freeUser.FreeUserActivity
import com.khs.nbbang.kakaoUser.KakaoUserActivity
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
                launch<FreeUserActivity>(startForResult, null)
            }
            LoginType.TYPE_KAKAO -> {
                launch<KakaoUserActivity>(startForResult, null)
            }
        }
    }

    fun checkLogin() {
        loginCookieInject.cookieData
    }
}