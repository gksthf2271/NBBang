package com.khs.nbbang.login

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.kakaoFriends.KakaoView
import com.khs.nbbang.kakaoFriends.ReturnType
import com.khs.nbbang.user.KaKaoMember
import com.khs.nbbang.user.KaKaoUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val mContext: Context) : BaseViewModel(), KakaoView{

    private val _myDataFromKakao: MutableLiveData<KaKaoUser> = MutableLiveData()
    private val _isLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val _friendList : MutableLiveData<ArrayList<KaKaoMember>> = MutableLiveData()

    val gIsLogin: LiveData<Boolean> get() = _isLogin
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

    fun login(context: Context) {
        handleLogin(context, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    private fun updateMyDataFromKakao(user : User) {
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
                   handleMyInfoUpdate(Schedulers.io(), AndroidSchedulers.mainThread())
                    _isLogin.postValue(true)
                }
            }
            ReturnType().RETURN_TYPE_LOGOUT_SUCCESS -> {
                Log.v(TAG,"RETURN_TYPE_LOGOUT_SUCCESS, Result : $result")
                logoutAndResetData()
            }
            ReturnType().RETURN_TYPE_MY_INFO_SUCCESS -> {
                Log.v(TAG,"RETURN_TYPE_MY_INFO_SUCCESS, Result : $result")
                updateMyDataFromKakao(result as User)
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
            ReturnType().RETURN_TYPE_CHECK_TOKEN_FAILED -> {
                Log.v(TAG,"RETURN_TYPE_CHECK_TOKEN, Result : $result")
                logoutAndResetData()
            }
            ReturnType().RETURN_TYPE_CHECK_TOKEN_SUCCESS -> {
                handleMyInfoUpdate(Schedulers.io(), AndroidSchedulers.mainThread())
                _isLogin.postValue(true)
            }
        }
    }

    fun checkKakaoLoginBySdk(context: Context) {
        handleCheckHasToken(context, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    fun checkKakaoLoginByLocalValue() : Boolean{
        return _isLogin.value == true
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}