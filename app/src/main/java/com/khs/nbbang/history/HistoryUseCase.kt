package com.khs.nbbang.history

import com.khs.nbbang.history.data.AddHistoryRequest
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.db.HistoryServices
import com.khs.nbbang.history.db.NBBangGateway
import io.reactivex.rxjava3.core.Single

/**
 * UseCase의 역할
 *  I/O level과 상관없이 기능자체만 구현된 부분.
 */

interface GetNbbangHistory : NBBangGateway, HistoryServices {
    fun getNBBangHistory(): Single<GetNBBangHistoryResult> = get().map {
        with(calculateProgress(it)) {
            GetNBBangHistoryResult(it)
        }
    }
}

interface AddNBBangHistory : NBBangGateway {
    fun addNBBangHistory(request: AddHistoryRequest): Single<NBBangHistory> = add(
        request.date,
        request.peopleCount,
        request.totalPrice,
        request.joinPeople,
        request.description
    )
}
