package com.khs.nbbang.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.AuthType
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.kakaoFriends.KakaoView
import com.khs.nbbang.kakaoFriends.kakao_interface.ReturnType
import com.khs.nbbang.user.KaKaoMember
import com.khs.nbbang.user.KaKaoUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(val mContext: Context) : BaseViewModel(), KakaoView{

    private val _myDataFromKakao: MutableLiveData<KaKaoUser> = MutableLiveData()
    private val _loginCookie: MutableLiveData<LoginCookie> = MutableLiveData()
    private val _isLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val _friendList : MutableLiveData<ArrayList<KaKaoMember>> = MutableLiveData()

    val gIsLogin: LiveData<Boolean> get() = _isLogin
    val gLoginCookie: LiveData<LoginCookie> get() = _loginCookie
    val gMyData: LiveData<KaKaoUser> get() = _myDataFromKakao
    val gFriendList: LiveData<ArrayList<KaKaoMember>> get() = _friendList

    fun resetMyData() {
        _myDataFromKakao.value = null
    }

    fun logoutAndResetData() {
        _isLogin.postValue(false)
        resetMyData()
    }

    fun freeUser() {
        Log.v(TAG, "freeUser(...)")
        logoutAndResetData()
    }

    fun login() {
        if (LoginClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            // 카카오톡으로 로그인
            LoginClient.instance.loginWithKakaoTalk(mContext) { token, error ->
                checkLogin(token, error)
            }
        } else {
            Toast.makeText(mContext, "카카오톡 설치 필요!", Toast.LENGTH_SHORT).show()
            LoginClient.instance.loginWithKakaoAccount(mContext) { token, error ->
                checkLogin(token, error)
            }
        }
    }

    private fun checkLogin(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e(TAG, "로그인 실패", error)
            logoutAndResetData()
        } else if (token != null) {
            Log.i(TAG, "로그인 성공 ${token.accessToken}")
            updateMyDataFromKakao()
            _loginCookie.postValue(LoginCookie(accessToken = token.accessToken))
            _isLogin.postValue(true)
        }
    }

    private fun updateMyDataFromKakao() {
        UserApiClient.instance.me { user, throwable ->
            if (user == null) {
                Log.e(TAG, "Failed loadMyData from KaKao : $throwable")
                return@me
            } else {
                var name = user.properties.let { it!!.get("nickname") }
                _myDataFromKakao.postValue(
                    KaKaoUser(
                        id = user.id,
                        name = name ?: "",
                        properties = user.properties,
                        kakaoAccount = user.kakaoAccount,
                        groupUserToken = user.groupUserToken,
                        connectedAt = user.connectedAt,
                        synchedAt = user.synchedAt
                    )
                )
            }
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
                    _isLogin.postValue(true)
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
                    logoutAndResetData()
                }
            }
        }
    }

    fun logout() {
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                logoutAndResetData()
            }
        }
    }

    // 카카오톡 프로필 가져오기
    fun loadProfileInfo() {
        TalkApiClient.instance.profile { profile, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡 프로필 가져오기 실패", error)
            } else if (profile != null) {
                Log.i(
                    TAG, "카카오톡 프로필 가져오기 성공" +
                            "\n닉네임: ${profile.nickname}" +
                            "\n프로필사진: ${profile.thumbnailUrl}" +
                            "\n국가코드: ${profile.countryISO}"
                )
            }
        }
    }

    fun loadFriendList() {
        handleLoadKakaoFriendList(mContext, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    override fun renderKakaoMembers(kakaoFirends: ArrayList<KaKaoMember>) {
        _friendList.value = kakaoFirends
    }

    override fun requestResult(resultCode: Int, result: Any?) {
        when(resultCode) {
            ReturnType().RETURN_TYPE_SUCCESS -> {
                Log.v(TAG,"RETURN_TYPE_SUCCESS, Result : $result")
            }
            ReturnType().RETURN_TYPE_FAILED -> {
                Log.v(TAG,"RETURN_TYPE_FAILED, Result : $result")
            }
        }
    }
}