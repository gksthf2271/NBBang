package com.khs.nbbang.utils

import android.util.Log

object LogUtil {
    val TAG_UI = "NBB_UI"
    val TAG_NETWORK = "NBB_NETWORK"
    val TAG_SEARCH = "NBB_SEARCH"
    val TAG_PAY = "NBB_PAY"
    val TAG_DEFAULT = "NBB_DEFAULT"


    fun iLog(tag: String?, className: String, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.i(localTAG, msg)
    }

    fun dLog(tag: String?, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.d(localTAG, msg)
    }

    fun eLog(tag: String?, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.e(localTAG, msg)
    }

    fun vLog(tag: String?, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.v(localTAG, msg)
    }

    fun wLog(tag: String?, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.w(localTAG, msg)
    }
}