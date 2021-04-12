package com.khs.nbbang.kakaoFriends.kakao_interface

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.khs.nbbang.login.LoginCookie
import io.reactivex.rxjava3.core.Single

interface KakaoGatewayImpl : KakaoGateway {

    override fun login(context: Context, callback: (token: OAuthToken?, error: Throwable?) -> Unit){
        if (LoginClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡으로 로그인
            LoginClient.instance.loginWithKakaoTalk(context, callback =  callback)
        } else {
            Toast.makeText(context, "카카오톡 설치 필요!", Toast.LENGTH_SHORT).show()
            LoginClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

//    private fun checkLogin(token: OAuthToken?, error: Throwable?) {
//        if (error != null) {
//            Log.e("KakaoGatewayImpl", "로그인 실패", error)
//            logoutAndResetData()
//        } else if (token != null) {
//            Log.i("KakaoGatewayImpl", "로그인 성공 ${token.accessToken}")
//            updateMyDataFromKakao()
//            _loginCookie.postValue(LoginCookie(accessToken = token.accessToken))
//            _isLogin.postValue(true)
//        }
//    }
}