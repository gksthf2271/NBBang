package com.khs.nbbang.search

import android.content.Context
import androidx.room.rxjava3.EmptyResultSetException
import com.khs.nbbang.search.response.LocalSearchModel
import com.khs.nbbang.utils.LogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


interface KakaoLocalView : SearchKeyword, GetKeywords, UpdateKeyword, DeleteKeyword, SaveKeyword{
    val compositeDisposable: CompositeDisposable

    fun renderSearchKeywords(searchResult: LocalSearchModel)
    fun renderKeywordsHistory(searchResult: GetSearchAllResult)

    fun handleSearchKeyword(context: Context, keyword: String) {
        val d = searchKeyword(keyword)
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                handleInsertKeywordHistory(context, keyword)
            }
            .onErrorComplete{
                return@onErrorComplete true
            }
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

    fun handleInsertKeywordHistory(context: Context, keyword: String, isRefreshUI: Boolean = true) {
        val checkDisposable = getKeyword(keyword)
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext { t ->
                LogUtil.eLog(LogUtil.TAG_CONTROL_CONTAINER, this.javaClass.simpleName, "insert Error : $t")
                if (t is EmptyResultSetException) {
                    return@onErrorResumeNext SingleSource {
                        val insertDisposable = insert(keyword)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { r ->
                                if (isRefreshUI) {
                                    handleGetKeywordHistory(context)
                                }
                            }
                        compositeDisposable.add(insertDisposable)
                    }
                } else {
                    return@onErrorResumeNext Single.error(t)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {  r ->
                handleUpdateKeywordHistory(context, r)
            }
        compositeDisposable.add(checkDisposable)
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}