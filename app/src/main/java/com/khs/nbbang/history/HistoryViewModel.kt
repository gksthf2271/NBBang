package com.khs.nbbang.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khs.nbbang.base.BaseViewModel
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.db_interface.NBBangGatewayImpl
import com.khs.nbbang.history.db_interface.NBBangHistoryView
import com.khs.nbbang.history.room.AppDatabase
import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.utils.DateUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.Year
import java.util.*

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
        Log.v(TAG,"renderHistory(...)")
        _showLoadingView.value = false
        _history.postValue(nbbangHistory)
    }

    override val mNBBPlaceDao: NBBPlaceDao
        get() = _db.value.let { it!!.nbbangDao() }

    override val mNBBMemberDao: NBBMemberDao
        get() = _db.value.let { it!!.nbbMemberDao()}

    fun showHistoryByMonth(month: Int) {
        _showLoadingView.value = true
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
        val month = _selectMonth.value!!.plus(1)
        if (month > 12) return
        _selectMonth.postValue(month)
        Log.v(TAG,"increaseMonth(...) : ${_selectMonth.value}")
    }

    fun decreaseMonth() {
        val month = _selectMonth.value!!.minus(1)
        if (month< 1) return
        _selectMonth.postValue(month)
        Log.v(TAG,"decreaseMonth(...) : ${_selectMonth.value}")
    }

    override fun onCleared() {
        super.onCleared()
        handleDestroy()
    }
}