package com.khs.nbbang.base

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.khs.nbbang.PackageRepository
import com.khs.nbbang.login.LoginCookie
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.SelectPageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

open class NBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "{1ba03cee909ded713fcf50d73b1d2ce9}")

        startKoin {
            androidLogger()
            androidContext(this@NBApp)
            modules(dataModel, viewModel)
        }
    }

    val dataModel = module {
        single {
            PackageRepository(applicationContext)
        }
        single{
            LoginCookie()
        }
    }

    val viewModel = module {
        viewModel {
            LoginViewModel(get())
        }

        viewModel {
            SelectPageViewModel(get())
        }
    }
}

////viewModel을 주입하기 위한 module
//val myViewModel = module {
//    //vieModel 정의
//    viewModel {
//        ExampleViewModel(get()) //PrintService를 get()으로 주입
//    }
//}
//
//val myModule = module {
//    single {
//        PackageRepository(androidContext())
//    }
//
//    single {
//        PrintService(get())
//    }
//
//    factory {
//        InjectCountData()
//    }