package com.khs.nbbang.search

import com.khs.nbbang.search.response.LocalSearchModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalAPI {
    @GET("/v2/local/search/keyword.json")
    fun searchKeyword(
        @Query("query") query: String
    ): Observable<LocalSearchModel>
}
