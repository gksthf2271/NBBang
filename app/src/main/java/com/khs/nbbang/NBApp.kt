package com.khs.nbbang

import android.app.Application
import android.util.Log
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
import lv.chi.photopicker.ChiliPhotoPicker
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
 *  1. 갤러리 연동하여, 프로필 설정 할 수 있는 기능 추가                                                    ---> 완료
 *  2. 카카오 친구목록 UI 구현                                                                        ---> 작업중(카카오 API 값과, SelectMemberView 동기화 해야됨)
 *  3. ddPeopleFragment AddMemberView 출력 시 하단 FavoriteMember 보이고, 선택하여 추가 할 수 있는 기능 구현 ---> 완료
 *  4. UI 정리(테마 변경)                                                                            ---> 점진적 작업중
 *  5. AddPlaceFragment UI 일관성 유지되도록 수정 FloatingButton에 MotionView                           ---> 완료, 중복소스 존재
 *  6. AddPlaceRecyclerViewAdapter 에서 motionLayout 처리 간 이슈 발생                                 ---> 완료
 *  7. 카카오 로그인 lifecycler 확립 및 적용                                                            ---> 완료
 *  Group관리 기능은 2.0에 구현
 * **/


open class NBApp : Application(){
    private val TAG = this.javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG,"NBApp Start!, onCreate(...)")

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
                .baseUrl("http://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create(get()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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