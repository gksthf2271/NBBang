package com.khs.nbbang.search

import android.content.Context
import com.khs.nbbang.search.response.LocalSearchModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


interface KakaoLocalView : SearchKeyword, GetKeywords, UpdateKeyword, DeleteKeyword, SaveKeyword{
    val compositeDisposable: CompositeDisposable

    fun renderSearchKeywords(searchResult: LocalSearchModel)
    fun renderKeywordsHistory(searchResult: GetSearchAllResult)

    fun handleSearchKeyword(context: Context, keyword: String) {
        val d = searchKeyword(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { r ->
                renderSearchKeywords(r)
            }
        compositeDisposable.add(d)
    }

    fun handleGetKeywordHistory(context: Context) {
        val d = getKeywords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { r ->
                renderKeywordsHistory(r)
            }
        compositeDisposable.add(d)
    }

    fun handleUpdateKeywordHistory(context: Context, keyword: GetSearchResult) {
        var searchCount = keyword.kakaoSearchKeyword.searchCount
        val d = updateKeywordCount(keyword.kakaoSearchKeyword.keyword, ++searchCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { r ->
                handleGetKeywordHistory(context)
            }
        compositeDisposable.add(d)
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}