package com.khs.nbbang.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.AuthType
import com.kakao.sdk.user.UserApiClient
import com.khs.nbbang.base.BaseViewModel

class LoginViewModel(context: Context) : BaseViewModel() {

    private val _mLoginLiveData: MutableLiveData<LoginCookie> = MutableLiveData()
    var mContext : Context

    init {
        mContext = context
    }

    val mLoginCookie: LiveData<LoginCookie>
        get() = _mLoginLiveData

    fun checkCookie() {

    }

    fun login() {
        if (LoginClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            // 카카오톡으로 로그인
            LoginClient.instance.loginWithKakaoTalk(mContext) { token, error ->
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

    fun reLogin() {
        if (LoginClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            LoginClient.instance.loginWithKakaoAccount(
                mContext, authType = AuthType.REAUTHENTICATE
            ) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                }
            }
        }
    }

    fun disconnectLoginSession() {
        // 연결 끊기
        if (LoginClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e(TAG, "연결 끊기 실패", error)
                } else {
                    Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                }
            }
        }
    }

    fun logout() {
        // 로그아웃
        if (LoginClient.instance.isKakaoTalkLoginAvailable(mContext)) {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
        }
    }
}