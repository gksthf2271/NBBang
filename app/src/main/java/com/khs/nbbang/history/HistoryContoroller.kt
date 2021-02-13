package com.khs.nbbang.history

import com.khs.nbbang.history.db.AddHistoryRequest
import com.khs.nbbang.history.db.NBBangHistory

interface HistoryContoroller {
    fun requestAddTodo(
        currentMs: Long,
        nbbangHistory: NBBangHistory
    ): AddHistoryRequest = AddHistoryRequest(
        currentMs,
        nbbangHistory.peopleCount,
        nbbangHistory.totalPrice,
        nbbangHistory.joinPeople,
        nbbangHistory.place,
        nbbangHistory.description,
        nbbangHistory.done
    )
}