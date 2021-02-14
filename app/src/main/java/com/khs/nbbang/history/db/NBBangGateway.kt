package com.khs.nbbang.history.db

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

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