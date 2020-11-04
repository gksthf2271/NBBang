package com.khs.nbbang.login

import android.util.Log
import com.khs.nbbang.base.BaseView

class LoginView : BaseView{
    val TAG = this.javaClass.name

    override fun showError(error: Int) {
        Log.v(TAG,"showError(...)")
    }
}