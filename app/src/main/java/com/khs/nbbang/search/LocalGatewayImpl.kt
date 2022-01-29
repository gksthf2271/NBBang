package com.khs.nbbang.search

import com.khs.nbbang.history.db_interface.NBBangDaoProvider
import com.khs.nbbang.history.room.NBBSearchKeywordDataModel
import com.khs.nbbang.search.response.LocalSearchModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LocalGatewayImpl : LocalGateway, NBBKakaoAPIProvider, NBBangDaoProvider {
    //Remote
    override fun search(keywords: String): Observable<LocalSearchModel> = mKakaoLocalAPI.searchKeyword(keywords)

    //Local
    override fun getAllSearchKeywords(): Single<GetSearchAllResult> =
        mNBBKeywordsDao.getKeywords().map {
            GetSearchAllResult(it.map { NBBSearchKeywordDataModel ->
                GetSearchResult(
                    KakaoSearchKeyword(
                    id = NBBSearchKeywordDataModel.id ?: 0,
                    keyword = NBBSearchKeywordDataModel.keyword,
                    searchCount = NBBSearchKeywordDataModel.searchCount ?: 0)
                )
            })
        }

    override fun getCount(keyword: String): Single<GetSearchResult> =
        mNBBKeywordsDao.getKeywordCount(keyword).map {
            GetSearchResult(KakaoSearchKeyword(
                id = it.id ?: 0,
                keyword = it.keyword,
                searchCount = it.searchCount ?: 0
            ))
        }

    override fun insert(keyword: String): Single<Long> = mNBBKeywordsDao.insert(
        NBBSearchKeywordDataModel(
            id = null,
            keyword = keyword,
            searchCount = 1
        )
    )


    override fun delete(keyword: String) {
        mNBBKeywordsDao.delete(keyword = keyword)
    }

    override fun deleteAll() {
        mNBBKeywordsDao.deleteAll()
    }

    override fun update(keyword: String, searchCount: Int): Single<Int> = mNBBKeywordsDao.update(keyword, searchCount)
}