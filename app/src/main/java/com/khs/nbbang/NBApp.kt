package com.khs.nbbang

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.login.LoginCookie
import com.khs.nbbang.login.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 *  inject() 의존성 주입 - Lazy 방식
val bb_inject1 : BB by inject()	// inject Type 유형 1 - Type by inject()
val bb_inject2 by inject<BB>()	// inject Type 유형 2 - by inject<Type>()

 * get() 의존성 주입 - 바로 주입 방식
var bb_get1 : BB = get()		// get Tpye 유형 1 - Type = get()
var bb_get2 = get<BB>()		// get Type 유형 2 - get<Type>()

 * Inject와 get 방식의 차이
inject - Lazy 방식의 주입, 해당 객체가 사용되는 시점에 의존성 주입
get - 바로 주입, 해당 코드 실행시간에 바로 객체를 주입
 */

open class NBApp : Application(){
    val TAG = this.javaClass.name

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG,"NBApp Start!, onCreate(...)")

        // Kakao SDK 초기화
        KakaoSdk.init(this, "1ba03cee909ded713fcf50d73b1d2ce9")

        startKoin {
            androidLogger()
            androidContext(this@NBApp)
            modules(listOf(databaseModule, dataModule, viewModelModule))
        }
    }

    val databaseModule = module {
        fun provideDatabase(application: Application): AppDatabase {
            return AppDatabase.getInstance(application)
        }

        fun provideCountriesDao(database: AppDatabase): NBBPlaceDao {
            return database.nbbangDao()
        }
        single { provideDatabase(androidApplication())}
        single { provideCountriesDao(get()) }
    }

    val dataModule = module {
        single {
            PackageRepository(applicationContext)
        }
    }

    val viewModelModule = module {
        viewModel {
            LoginViewModel(get())
        }

        viewModel {
            PageViewModel(get())
        }

        viewModel {
            HistoryViewModel(get())
        }
    }
}