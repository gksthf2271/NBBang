package com.khs.nbbang.history

import com.khs.nbbang.history.db.*
import io.reactivex.rxjava3.core.Single

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
        request.place,
        request.description,
        request.done
    )
}
