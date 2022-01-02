package com.khs.nbbang.search

import com.khs.nbbang.search.response.LocalSearchModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalAPI {
    @GET("/v2/local/search/keyword.{FORMAT}")
    fun searchKeyword(
        @Query("FORMAT") query: String
    ): Observable<LocalSearchModel>
}
