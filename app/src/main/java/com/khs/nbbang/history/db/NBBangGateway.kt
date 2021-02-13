package com.khs.nbbang.history.db

import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.ItemObj.Place
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
        joinPeople: List<People>,
        place: List<Place>,
        description: String,
        done: Boolean
    ): Single<NBBangHistory>
}