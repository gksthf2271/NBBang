package com.khs.nbbang.history.db

import com.khs.nbbang.history.AddNBBangHistory
import com.khs.nbbang.history.GetNbbangHistory
import com.khs.nbbang.history.HistoryContoroller
import com.khs.nbbang.history.HistoryPresenter
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

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

    fun handleAddNBBangHistory(sub: Scheduler, ob: Scheduler, nbbangHistory: NBBangHistory) {
        val d = addNBBangHistory(requestAddTodo(System.currentTimeMillis(), nbbangHistory))
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