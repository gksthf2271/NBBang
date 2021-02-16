package com.khs.nbbang.history.db_interface

import com.khs.nbbang.history.AddNBBangHistory
import com.khs.nbbang.history.GetNbbangHistory
import com.khs.nbbang.history.HistoryContoroller
import com.khs.nbbang.history.HistoryPresenter
import com.khs.nbbang.history.data.AddHistoryRequest
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * NBBangHistoryView 역할
 *  - NBBangHistoryView는 android의 개념적 UI View(activity,fragment 등...)이 아님.
 *  - 가상의 I/O Channel을 담당하는 개념적 프로그래밍
 *  - 개념적 프로그래밍이란? : DB, AOS, IOS, WEB 상관없이 사용되는 일련의 로직을 담고있음.
 */

interface NBBangHistoryView : AddNBBangHistory, GetNbbangHistory, HistoryContoroller,
    HistoryPresenter {

    val compositeDisposable: CompositeDisposable

    fun renderHistorys(nbbangHistory: GetNBBangHistoryResult)

    fun showHistory(sub: Scheduler, ob: Scheduler) {
        val d = getNBBangHistory()
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                renderHistorys(
                    presentHistory(r)
                )
            }
        compositeDisposable.add(d)
    }

    fun handleAddNBBangHistory(sub: Scheduler, ob: Scheduler, addHistoryRequest: AddHistoryRequest) {
        val d = addNBBangHistory(addHistoryRequest)
            .flatMap { getNBBangHistory() }
            .subscribeOn(sub)
            .observeOn(ob)
            .subscribe { r ->
                renderHistorys(
                    presentHistory(r)
                )
            }
        compositeDisposable.add(d)
    }

    fun handleDestroy() {
        compositeDisposable.dispose()
    }
}