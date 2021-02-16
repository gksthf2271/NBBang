package com.khs.nbbang.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.db_interface.NBBangHistoryView
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBPlaceDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel(database: AppDatabase) : BaseViewModel(), NBBangHistoryView,
    NBBangGatewayImpl {
    private val _db : MutableLiveData<AppDatabase> = MutableLiveData()
    val mDB : LiveData<AppDatabase> get() = _db

    private val _history : MutableLiveData<GetNBBangHistoryResult> = MutableLiveData()
    val mHistory : LiveData<GetNBBangHistoryResult> get() = _history

    init {
        _db.postValue(database)
    }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    override fun renderHistorys(nbbangHistory: GetNBBangHistoryResult) {
        Log.v(TAG,"renderHistory(...)")
        _history.postValue(nbbangHistory)
    }

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value.let { it!!.nbbangDao() }

    fun showHistory() {
        handleShowHistory(
            Schedulers.io(),
            AndroidSchedulers.mainThread()
        )
    }


    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}