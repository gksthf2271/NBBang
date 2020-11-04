package com.khs.nbbang.login

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.AuthType
import com.khs.nbbang.base.BaseViewModel

class LoginViewModel : BaseViewModel() {

    fun login(context: Context) {
        if (LoginClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡으로 로그인
            LoginClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            Log.v(TAG,"카카오톡 설치 필요")
        }
    }

    fun reLogin(context: Context) {
        LoginClient.instance.loginWithKakaoAccount(context, authType = AuthType.REAUTHENTICATE
        ) { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
            }
        }
    }
}