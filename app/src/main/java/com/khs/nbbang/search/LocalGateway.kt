package com.khs.nbbang.search

import com.khs.nbbang.search.response.LocalSearchModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LocalGateway {
    //Remote
    fun search(keywords: String) : Observable<LocalSearchModel>

    //Local
    fun getCount(keyword: String) : Single<GetSearchResult>
    fun getAllSearchKeywords() : Single<GetSearchAllResult>
    fun insert(keyword: String): Single<Long>
    fun delete(keyword: String)
    fun deleteAll()
    fun update(keyword: String, searchCount: Int) : Single<Int>
}