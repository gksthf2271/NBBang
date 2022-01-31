package com.khs.nbbang.search

import com.khs.nbbang.search.response.LocalSearchModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface SearchKeyword : LocalGateway {
    fun searchKeyword(keyword: String): Observable<LocalSearchModel> = search(keyword)
}

interface GetKeywords : LocalGateway {
    fun getKeywordHistory(keyword: String): Single<GetSearchResult> = getKeyword(keyword)
    fun getKeywords(): Single<GetSearchAllResult> = getAllSearchKeywords()
}

interface SaveKeyword : LocalGateway {
    fun saveKeyword(keyword: String) = insert(keyword)
}

interface DeleteKeyword : LocalGateway {
    fun deleteKeyword(keyword: String) = delete(keyword)
    fun deleteAllKeyword() = deleteAll()
}

interface UpdateKeyword: LocalGateway {
    fun updateKeywordCount(keyword: String, count: Int) = update(keyword, count)
}