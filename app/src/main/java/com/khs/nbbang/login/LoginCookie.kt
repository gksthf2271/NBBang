package com.khs.nbbang.login

import android.util.Log

class LoginCookie {
    var cookieData: String?

    constructor() {
        this.cookieData = null
    }

    fun printLoginCookie() {
        Log.v("printLoginCookie", "cookieData :  $cookieData")
    }
}