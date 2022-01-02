package com.khs.nbbang.search

data class GetSearchAllResult(val list: List<GetSearchResult>)

data class GetSearchResult(val kakaoSearchKeyword: KakaoSearchKeyword)

data class KakaoSearchKeyword(val id: Long, val keyword: String, val searchCount: Int)