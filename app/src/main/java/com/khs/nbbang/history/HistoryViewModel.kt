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
import io.reactivex.rxjava3.disposables.CompositeDisposable

class HistoryViewModel(database: AppDatabase) : BaseViewModel(), NBBangHistoryView,
    NBBangGatewayImpl {
    private val _db : MutableLiveData<AppDatabase> = MutableLiveData()
    val mDB : LiveData<AppDatabase> get() = _db

    init {
        _db.postValue(database)
    }

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    override fun renderHistorys(nbbangHistory: GetNBBangHistoryResult) {
        Log.v(TAG,"renderHistory(...)")
    }

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value.let { it!!.nbbangDao() }


    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}