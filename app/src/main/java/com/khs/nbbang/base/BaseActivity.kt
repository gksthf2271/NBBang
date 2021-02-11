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
    val TAG = this.javaClass.name
    val DEBUG = true
    val KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE"

    inline fun <reified I : Activity> launch(resultLauncher: ActivityResultLauncher<Intent>, loginType: LoginType?) {
        resultLauncher.launch(Intent(applicationContext, I::class.java).putExtra(KEY_LOGIN_TYPE, loginType))
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (DEBUG) Toast.makeText(this, this.javaClass.name + " 활성화 됨.", Toast.LENGTH_SHORT).show()
            }
        }

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