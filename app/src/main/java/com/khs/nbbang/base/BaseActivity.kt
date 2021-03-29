package com.khs.nbbang.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.khs.nbbang.login.LoginType

open class BaseActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    val DEBUG = true
    val KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE"
    var gIsRunningActivity : Boolean = false

    inline fun <reified I : Activity> launch(resultLauncher: ActivityResultLauncher<Intent>) {
        resultLauncher.launch(Intent(applicationContext, I::class.java))
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (DEBUG) Toast.makeText(this, this.javaClass.simpleName + " 활성화 됨.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gIsRunningActivity = true
    }

    override fun onStart() {
        Log.v(TAG,"onStart(...)")
        gIsRunningActivity = true
        super.onStart()
    }

    override fun onPause() {
        Log.v(TAG,"onPause(...)")
        gIsRunningActivity = false
        super.onPause()
    }

    override fun onDestroy() {
        Log.v(TAG,"onDestroy(...)")
        gIsRunningActivity = false
        super.onDestroy()
    }

    override fun onResume() {
        Log.v(TAG,"onResume(...)")
        gIsRunningActivity = true
        super.onResume()
    }

    fun isRunningActivity() : Boolean{
        return gIsRunningActivity
    }
}