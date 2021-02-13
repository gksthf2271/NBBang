package com.khs.nbbang.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.history.db.AppDatabase

class HistoryViewModel(database: AppDatabase) : BaseViewModel() {
    private val _db : MutableLiveData<AppDatabase> = MutableLiveData()
    val mDB : LiveData<AppDatabase> get() = _db

    init {
        _db.postValue(database)
    }
}