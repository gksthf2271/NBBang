package com.khs.nbbang.history

import com.khs.nbbang.history.data.AddHistoryRequest
import com.khs.nbbang.history.data.NBBangHistory

/**
 * Controller의 역할
 *  - UI 환경의 input event를 받아 Usecase의 requestModel을 생성함.
 */

interface HistoryContoroller {
    fun requestAddHistory(
        currentMs: Long,
        nbbangHistory: NBBangHistory
    ): AddHistoryRequest =
        AddHistoryRequest(
            currentMs,
            nbbangHistory.peopleCount,
            nbbangHistory.totalPrice,
            nbbangHistory.joinPeople,
            nbbangHistory.description
        )
}