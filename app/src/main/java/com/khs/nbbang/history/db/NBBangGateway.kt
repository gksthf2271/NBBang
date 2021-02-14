package com.khs.nbbang.history.db

import com.khs.nbbang.history.data.NBBangHistory
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

/**
 * Gateway의 역할
 *  - UseCase가 DB에 직접적인 접근을 막고자 사용됨.
 */

interface NBBangGateway {
    fun get() : Single<List<NBBangHistory>>
    fun get(id: Long) : Maybe<NBBangHistory>
    fun remove(id: Long)
    fun add(
        date: Long,
        peopleCount: Int,
        totalPrice: Int,
        joinPeople: String,
        place: String,
        description: String,
        done: Boolean
    ): Single<NBBangHistory>
}