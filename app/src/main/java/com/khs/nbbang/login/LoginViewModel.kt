package com.khs.nbbang.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.kakaoFriends.KakaoView
import com.khs.nbbang.kakaoFriends.kakao_interface.ReturnType
import com.khs.nbbang.user.KaKaoMember
import com.khs.nbbang.user.KaKaoUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val mContext: Context) : BaseViewModel(), KakaoView{

    private val _myDataFromKakao: MutableLiveData<KaKaoUser> = MutableLiveData()
    private val _loginCookie: MutableLiveData<LoginCookie> = MutableLiveData()
    private val _isLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val _friendList : MutableLiveData<ArrayList<KaKaoMember>> = MutableLiveData()

    val gIsLogin: LiveData<Boolean> get() = _isLogin
    val gLoginCookie: LiveData<LoginCookie> get() = _loginCookie
    val gMyData: LiveData<KaKaoUser> get() = _myDataFromKakao
    val gFriendList: LiveData<ArrayList<KaKaoMember>> get() = _friendList

    private fun resetMyData() {
        _myDataFromKakao.value = null
    }

    private fun logoutAndResetData() {
        _isLogin.postValue(false)
        resetMyData()
    }

    fun freeUser() {
        Log.v(TAG, "freeUser(...)")
        logoutAndResetData()
    }

    fun login() {
        handleLogin(mContext, Schedulers.io(), AndroidSchedulers.mainThread())
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

    fun logout() {
        // 로그아웃
       handleLogout(Schedulers.io(), AndroidSchedulers.mainThread())
    }
    fun loadFriendList() {
        handleLoadKakaoFriendList(mContext, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    override fun renderKakaoMembers(kakaoFirends: ArrayList<KaKaoMember>) {
        _friendList.value = kakaoFirends
    }

    override fun requestResult(resultCode: Int, result: Any?) {
        when(resultCode) {
            ReturnType().RETURN_TYPE_NONE_SUCCESS -> {
                Log.v(TAG,"RETURN_TYPE_NONE_SUCCESS, Result : $result")
            }
            ReturnType().RETURN_TYPE_LOGIN_SUCCESS -> {
                Log.v(TAG,"RETURN_TYPE_LOGIN_SUCCESS, Result : $result")
               if (result != null && result is OAuthToken) {
                    Log.i(TAG, "로그인 성공 ${result.accessToken}")
                    updateMyDataFromKakao()
                    _loginCookie.postValue(LoginCookie(accessToken = result.accessToken))
                    _isLogin.postValue(true)
                }
            }
            ReturnType().RETURN_TYPE_PROFILE_SUCCESS -> {
                Log.v(TAG,"RETURN_TYPE_PROFILE_SUCCESS, Result : $result")
            }
            ReturnType().RETURN_TYPE_NONE_FAILED -> {
                Log.v(TAG,"RETURN_TYPE_FAILED, Result : $result")
            }
            ReturnType().RETURN_TYPE_LOGIN_FAILED -> {
                Log.v(TAG,"RETURN_TYPE_FAILED, Result : $result")
                Log.e(TAG, "로그인 실패, ", result as Throwable)
                logoutAndResetData()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}