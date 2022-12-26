package com.khs.nbbang.history

import com.khs.nbbang.history.data.AddHistoryRequest
import com.khs.nbbang.history.data.GetNBBangHistoryResult
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.db_interface.NBBangGateway
import io.reactivex.rxjava3.core.Single

/**
 * UseCase의 역할
 *  I/O level과 상관없이 기능자체만 구현된 부분.
 */

interface GetNbbangHistory : NBBangGateway {
    fun getNBBangAllHistory(): Single<GetNBBangHistoryResult> = get().map {
        GetNBBangHistoryResult(it)
    }

    fun getNBBangHistoryByMonth(minTimeMs:Long, maxTimeMs:Long): Single<GetNBBangHistoryResult> = get(minTimeMs, maxTimeMs).map {
        GetNBBangHistoryResult(it)
    }
}

interface AddNBBangHistory : NBBangGateway {
    fun addNBBangHistory(request: AddHistoryRequest): Single<NBBangHistory> = add(
        request.date,
        request.place,
        request.dutchPay,
        request.description
    )
}
