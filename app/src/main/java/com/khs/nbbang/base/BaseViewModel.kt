package com.khs.nbbang.base

import android.util.Log
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val TAG = this.javaClass.name

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG,"onCleared(...)")
    }
}