package com.khs.nbbang.base

import androidx.lifecycle.ViewModel
import com.khs.nbbang.utils.LogUtil

open class BaseViewModel : ViewModel() {
    val TAG_CLASS = this.javaClass.simpleName
    val LOG_TAG = LogUtil.TAG_VIEW_MODEL

    override fun onCleared() {
        super.onCleared()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onCleared(...)")
    }
}