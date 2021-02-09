package com.khs.nbbang

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.khs.nbbang.freeUser.viewModel.PageViewModel
import com.khs.nbbang.login.LoginCookie
import com.khs.nbbang.login.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

open class NBApp : Application() {
    val TAG = this.javaClass.name
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG,"NBApp Start!, onCreate(...)")

        // Kakao SDK 초기화
        KakaoSdk.init(this, "{1ba03cee909ded713fcf50d73b1d2ce9}")

        startKoin {
            androidLogger()
            androidContext(this@NBApp)
            modules(listOf(dataModule, viewModelModule))
        }
    }

    val dataModule = module {
        single {
            PackageRepository(applicationContext)
        }

        single{
            LoginCookie()
        }
    }

    val viewModelModule = module {
        viewModel {
            LoginViewModel(get())
        }

        viewModel {
            PageViewModel(get())
        }
    }
}