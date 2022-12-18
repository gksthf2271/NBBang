package com.khs.nbbang

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.common.KakaoSdk
import com.khs.nbbang.history.HistoryViewModel
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.history.room.NBBSearchKeywordsDao
import com.khs.nbbang.localMember.MemberManagementViewModel
import com.khs.nbbang.login.LoginViewModel
import com.khs.nbbang.page.viewModel.PageViewModel
import com.khs.nbbang.page.viewModel.SelectMemberViewModel
import com.khs.nbbang.search.KakaoLocalAPI
import com.khs.nbbang.search.KakaoLocalViewModel
import com.khs.nbbang.utils.GlideUtils
import com.khs.nbbang.utils.LogUtil
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import lv.chi.photopicker.ChiliPhotoPicker
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

/**
 * TODO : 출시 전 해야될 것들
 *  1. 검색어 결과 물에 대한 Map 연동
 *  2. 장소 입력시 검색 기능 연동
 *  3. 입력된 장소 db 저장 및 추천 시슴템 도입
 *  4. 카카오 친구 추가 관련 가이드 UI 구현
 *  5. 성능 개선
 *  6. 사진 선택 라이브러리 제거 후 직접 구현
 * **/


class NBApp : Application(){
    private val TAG_CLASS = this.javaClass.simpleName
    private val LOG_TAG = LogUtil.TAG_UI

    override fun onCreate() {
        super.onCreate()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "NBApp Start!, onCreate(...)")

        // Kakao SDK 초기화
        KakaoSdk.init(this, "1ba03cee909ded713fcf50d73b1d2ce9")

        startKoin {
            androidLogger()
            androidContext(this@NBApp)
            modules(listOf(databaseModule, dataModule, viewModelModule, networkModule))
        }

        ChiliPhotoPicker.init(
            loader = GlideUtils.GideImageLoader(),
            authority = "com.khs.nbbang"
        )
    }

    private val networkModule = module {
        single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }

        single<Gson> { GsonBuilder().create() }

        single<OkHttpClient> {
            OkHttpClient.Builder().apply {
                cache(get())
                retryOnConnectionFailure(true)
                addInterceptor(get())
                addInterceptor(HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                })
            }.build()
        }

        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create(get()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(get())
                .build()
        }

        single {
            Interceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "KakaoAK e7def8ad592939fda6b271918916a9ec")
                        .build()
                )
            }
        }

        single<KakaoLocalAPI> {
            with(get() as Retrofit) {
                create(KakaoLocalAPI::class.java)
            }
        }
    }

    private val databaseModule = module {
        fun provideDatabase(application: Application): AppDatabase {
            return AppDatabase.getInstance(application)
        }

        fun provideNBBPlaceDao(database: AppDatabase): NBBPlaceDao {
            return database.nbbangDao()
        }

        fun provideNBBMemberDao(database: AppDatabase): NBBMemberDao {
            return database.nbbMemberDao()
        }

        fun provideNBBSearchKeywordDao(database: AppDatabase): NBBSearchKeywordsDao {
            return database.nbbSearchKeywordDao()
        }

        single { provideDatabase(androidApplication())}
        single { provideNBBPlaceDao(get()) }
        single { provideNBBMemberDao(get()) }
        single { provideNBBSearchKeywordDao(get())}
    }

    private val dataModule = module {
        single {
            PackageRepository(applicationContext)
        }
    }

    private val viewModelModule = module {
        viewModel {
            LoginViewModel(get())
        }

        viewModel {
            PageViewModel(get())
        }

        viewModel {
            HistoryViewModel(get())
        }

        viewModel {
            MemberManagementViewModel(get())
        }

        viewModel {
            SelectMemberViewModel()
        }
        viewModel {
            KakaoLocalViewModel(get(), get())
        }
    }
}