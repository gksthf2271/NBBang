package com.khs.nbbang.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.db_interface.NBBangHistoryView
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.history.room.NBBSearchKeywordsDao
import com.khs.nbbang.utils.DateUtils
import com.khs.nbbang.utils.LogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel(private val mDatabase: AppDatabase) : BaseViewModel(), NBBangHistoryView,
    NBBangGatewayImpl {
    private val _db : MutableLiveData<AppDatabase> = MutableLiveData()
    val mDB : LiveData<AppDatabase> get() = _db

    private val _selectMonth : MutableLiveData<Int> = MutableLiveData()
    val mSelectMonth : LiveData<Int> get() = _selectMonth

    private val _history : MutableLiveData<GetNBBangHistoryResult> = MutableLiveData()
    val mHistory : LiveData<GetNBBangHistoryResult> get() = _history

    private val _showLoadingView: MutableLiveData<Boolean> = MutableLiveData()
    val mShowLoadingView : LiveData<Boolean> get() = _showLoadingView

    init {
        _showLoadingView.value = false
        _db.value = mDatabase
        _selectMonth.value = DateUtils().currentMonth()
    }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    override fun renderHistorys(nbbangHistory: GetNBBangHistoryResult) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "renderHistory(...)")
        _history.postValue(nbbangHistory)
    }

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value!!.nbbangDao()

    override val mNBBMemberDao: NBBMemberDao
        get() = _db.value!!.nbbMemberDao()

    override val mNBBKeywordsDao: NBBSearchKeywordsDao
        get() = _db.value!!.nbbSearchKeywordDao()

    fun setCurrentMonthHistory() {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "setCurrentMonthHistory : ${DateUtils().currentMonth()}")
        _selectMonth.postValue(DateUtils().currentMonth())
    }

    fun showHistoryByMonth(month: Int) {
        updateLoadingFlag(true)
        handleShowHistoryByMonth(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            DateUtils().getTimeMsByMonth(month),
            DateUtils().getTimeMsByMonth(month+1) - 1
        )
    }

    fun showAllHistory() {
        _showLoadingView.value = true
        handleShowAllHistory(
            Schedulers.io(),
            AndroidSchedulers.mainThread()
        )
    }

    fun increaseMonth() {
        val month = _selectMonth.value?.plus(1) ?: return
        if (month > 12) return
        _selectMonth.postValue(month)
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "increaseMonth(...) : ${_selectMonth.value}")
    }

    fun decreaseMonth() {
        val month = _selectMonth.value?.minus(1) ?: return
        if (month< 1) return
        _selectMonth.postValue(month)
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "decreaseMonth(...) : ${_selectMonth.value}")
    }

    fun updateLoadingFlag(isShown : Boolean) {
        LogUtil.vLog(LOG_TAG, TAG_CLASS, "updateLoadingFlag : $isShown")
        _showLoadingView.value = isShown
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}