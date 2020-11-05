package com.khs.nbbang.base

import android.util.Log
import androidx.fragment.app.Fragment

open class BaseFragment :Fragment() {
    val TAG = this.javaClass.name

    override fun onStart() {
        super.onStart()
        Log.v(TAG,"onStart(...)")
    }
}