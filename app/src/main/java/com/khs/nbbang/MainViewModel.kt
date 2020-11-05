package com.khs.nbbang

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.nbbang.login.LoginCookie

class MainViewModel
//KoinComponent 를 사용한 주입이 아닌 생성자를 통해서 주입
    (val loginCookie: LoginCookie)
    : ViewModel() {

    val TAG = this.javaClass.simpleName

    private val _cookieData: MutableLiveData<LoginCookie> = MutableLiveData()

    val cookieData: LiveData<LoginCookie>
        get() = _cookieData

    fun resetCookie() {
        val exampleData = LoginCookie().apply {
            cookieData = ""
        }

        _cookieData.value = exampleData
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, ">>> onCleared")
    }
}