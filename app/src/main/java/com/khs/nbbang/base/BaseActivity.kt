package com.khs.nbbang.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.khs.nbbang.LoadingActivity
import com.khs.nbbang.utils.LogUtil

open class BaseActivity : AppCompatActivity() {
    val TAG_CLASS = this.javaClass.simpleName
    open val LOG_TAG = LogUtil.TAG_UI
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
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onStart(...)")
        gIsRunningActivity = true
        super.onStart()
    }

    override fun onPause() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onPause(...)")
        gIsRunningActivity = false
        super.onPause()
    }

    override fun onDestroy() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onDestroy(...)")
        gIsRunningActivity = false
        super.onDestroy()
    }

    override fun onResume() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "onResume(...)")
        gIsRunningActivity = true
        super.onResume()
    }

    fun isRunningActivity() : Boolean{
        return gIsRunningActivity
    }
}