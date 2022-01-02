package com.khs.nbbang.search

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface KakaoLocalView {
    val compositeDisposable: CompositeDisposable

//    fun handleSearchKeyword(context: Context, keyword: String) {
//        val d = KakaoLocalAPI.create()
//            .searchKeyword(keyword)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//        compositeDisposable.add(d)
//    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}