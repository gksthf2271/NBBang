package com.khs.nbbang.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.model.User
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.kakaoFriends.KakaoUserView
import com.khs.nbbang.kakaoFriends.ReturnType
import com.khs.nbbang.user.KaKaoMember
import com.khs.nbbang.user.KaKaoUser
import com.khs.nbbang.utils.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val mContext: Context) : BaseViewModel(), KakaoUserView{

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
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "freeUser(...)")
        logoutAndResetData()
    }

    fun login(context: Context) {
        handleLogin(context, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    private fun updateMyDataFromKakao(user : User) {
        val name = user.properties?.let { it["nickname"] }
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
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_NONE_SUCCESS, Result : $result")
            }
            ReturnType().RETURN_TYPE_LOGIN_SUCCESS -> {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_LOGIN_SUCCESS, Result : $result")
               if (result != null && result is OAuthToken) {
                   LogUtil.vLog(LOG_TAG, TAG_CLASS, "로그인 성공 ${result.accessToken}")
                   handleMyInfoUpdate(Schedulers.io(), AndroidSchedulers.mainThread())
                    _isLogin.postValue(true)
                }
            }
            ReturnType().RETURN_TYPE_LOGOUT_SUCCESS -> {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_LOGOUT_SUCCESS, Result : $result")
                logoutAndResetData()
            }
            ReturnType().RETURN_TYPE_MY_INFO_SUCCESS -> {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_MY_INFO_SUCCESS, Result : $result")
                updateMyDataFromKakao(result as User)
            }
            ReturnType().RETURN_TYPE_PROFILE_SUCCESS -> {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_PROFILE_SUCCESS, Result : $result")
            }
            ReturnType().RETURN_TYPE_NONE_FAILED -> {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_FAILED, Result : $result")
            }
            ReturnType().RETURN_TYPE_LOGIN_FAILED -> {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_FAILED, Result : $result")
                LogUtil.eLog(LOG_TAG, TAG_CLASS, "로그인 실패, ${result as Throwable}")
                logoutAndResetData()
            }
            ReturnType().RETURN_TYPE_CHECK_TOKEN_FAILED -> {
                LogUtil.vLog(LOG_TAG, TAG_CLASS, "RETURN_TYPE_CHECK_TOKEN, Result : $result")
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