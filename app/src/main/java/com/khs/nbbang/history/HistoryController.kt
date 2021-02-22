package com.khs.nbbang.history

import com.khs.nbbang.history.data.AddHistoryRequest
import com.khs.nbbang.history.data.NBBResultItem
import com.khs.nbbang.history.data.NBBangHistory

/**
 * Controller의 역할
 *  - UI 환경의 input event를 받아 Usecase의 requestModel을 생성함.
 */

interface HistoryController {
    fun requestAddHistory(
        currentMs: Long,
        nbbResultItem: NBBResultItem,
        description : String
    ): AddHistoryRequest =
        AddHistoryRequest(
            currentMs,
            nbbResultItem.place,
            nbbResultItem.dutchPay,
            description
        )
}