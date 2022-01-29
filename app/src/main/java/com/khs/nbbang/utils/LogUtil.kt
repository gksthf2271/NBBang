package com.khs.nbbang.utils

import android.util.Log

object LogUtil {
    val TAG_UI = "NBB_UI"
    val TAG_NETWORK = "NBB_NETWORK"
    val TAG_HISTORY = "NBB_HISTORY"
    val TAG_SEARCH = "NBB_SEARCH"
    val TAG_MEMBER = "NBB_MEMBER"
    val TAG_DUTCH_PAY = "NBB_DUTCH_PAY"
    val TAG_VIEW_MODEL = "NBB_VIEW_MODEL"
    val TAG_DEFAULT = "NBB"
    val TAG_CONTROL_CONTAINER = "NBB_CONTROL_CONTAINER"


    fun iLog(tag: String?, classTag: String, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.i(localTAG, "$classTag : $msg")
    }

    fun dLog(tag: String?, classTag: String, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.d(localTAG, "$classTag : $msg")
    }

    fun eLog(tag: String?, classTag: String, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.e(localTAG, "$classTag : $msg")
    }

    fun vLog(tag: String?, classTag: String, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.v(localTAG, "$classTag : $msg")
    }

    fun wLog(tag: String?, classTag: String, msg: String) {
        var localTAG = tag ?: TAG_DEFAULT
        Log.w(localTAG, "$classTag : $msg")
    }
}