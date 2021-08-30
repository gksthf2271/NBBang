package com.khs.nbbang.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.khs.nbbang.LoadingActivity

open class BaseActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    val DEBUG = true
    val KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE"
    val RESULT_FINISH = 1000
    var gIsRunningActivity : Boolean = false

    inline fun <reified I : Activity> launch(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(applicationContext, I::class.java)
        if(this is LoadingActivity){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        resultLauncher.launch(intent)
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when(result.resultCode){
                RESULT_FINISH -> finish()
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