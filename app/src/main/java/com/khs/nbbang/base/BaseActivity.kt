package com.khs.nbbang.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    val TAG = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG,"onStart(...)")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG,"onDestroy(...)")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"onResume(...)")
    }
}