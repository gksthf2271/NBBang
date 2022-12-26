package com.khs.nbbang.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.database.room.*
import com.khs.nbbang.database.db_interface.NBBangDaoProvider
import com.khs.nbbang.history.room.*
import com.khs.nbbang.search.response.LocalSearchModel
import com.khs.nbbang.utils.DateUtils
import com.khs.nbbang.utils.LogUtil
import io.reactivex.rxjava3.disposables.CompositeDisposable

class KakaoLocalViewModel(
    mInternalKakaoLocalApi: KakaoLocalAPI,
    mDatabase: AppDatabase
) : BaseViewModel(),
    KakaoLocalView,
    LocalGatewayImpl,
    NBBangDaoProvider,
    NBBKakaoAPIProvider{

    private val _db : MutableLiveData<AppDatabase> = MutableLiveData()
    private val _searchApi : MutableLiveData<KakaoLocalAPI> = MutableLiveData()
    private val _searchResult : MutableLiveData<LocalSearchModel> = MutableLiveData()
    private val _searchHistory : MutableLiveData<GetSearchAllResult> = MutableLiveData()

    val mDB : LiveData<AppDatabase> get() = _db
    val mSearchAPI : LiveData<KakaoLocalAPI> get() = _searchApi
    val mSearchResult : LiveData<LocalSearchModel> get() = _searchResult
    val mSearchHistory : LiveData<GetSearchAllResult> get() = _searchHistory

    override val mNBBHistoryDao: NBBHistoryDao
        get() = _db.value.let { it!!.nbbHistoryDao() }

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value.let { it!!.nbbPlaceDao() }

    override val mNBBMemberDao: NBBMemberDao
        get() = _db.value.let { it!!.nbbMemberDao()}

    override val mNBBKeywordsDao: NBBSearchKeywordsDao
        get() = _db.value.let { it!!.nbbSearchKeywordDao() }

    override val mKakaoLocalAPI: KakaoLocalAPI
        get() = _searchApi.value.let { it!! }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    init {
        _db.value = mDatabase
        _searchApi.value = mInternalKakaoLocalApi
    }

    override fun renderSearchKeywords(searchResult: LocalSearchModel) {
        val currentTime = System.currentTimeMillis()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "renderSearchKeywords(...) startTime : ${DateUtils.getDateByMillis(currentTime)}")
        _searchResult.postValue(searchResult)
    }

    override fun renderKeywordsHistory(searchResult: GetSearchAllResult) {
        val currentTime = System.currentTimeMillis()
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "renderKeywordsHistory(...) startTime : ${DateUtils.getDateByMillis(currentTime)}")
        _searchHistory.postValue(searchResult)
    }

    fun checkKeywordHistoryNotEmpty() : Boolean {
        return _searchHistory.value != null && !(_searchHistory.value as GetSearchAllResult).list.isNullOrEmpty()
    }

    fun removeKeywordHistory(context: Context, keyword: String, isRefreshUI : Boolean = true) {
        handleRemoveKeywordHistory(context, keyword, isRefreshUI)
    }

    fun removeAllKeywordHistory(context: Context, isRefreshUI : Boolean = true) {
        handleRemoveAllKeywordHistory(context, isRefreshUI = isRefreshUI)
    }

    fun searchKeyword(context: Context, keyword: String?) {
        if (keyword.isNullOrEmpty()) return
        handleSearchKeyword(context, keyword)
    }

    fun showSearchHistory(context: Context) {
        handleGetKeywordHistory(context)
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}