package com.khs.nbbang.history.db

import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.room.NBBangDataModel
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface NBBangGatewayImpl : NBBangGateway, NBBangDaoProvider{
    private fun convert(d: NBBangDataModel): NBBangHistory =
        NBBangHistory(
            d.id!!,
            d.date,
            d.peopleCount,
            d.totalPrice,
            d.joinPeople,
            d.place,
            d.description,
            d.done
        )
    override fun add(
        date: Long,
        peopleCount: Int,
        totalPrice: Int,
        joinPeople: String,
        place: String,
        description: String,
        done: Boolean
    ) : Single<NBBangHistory> = mNBBangDao.insert(
        NBBangDataModel(
            null,
            date,
            peopleCount,
            totalPrice,
            joinPeople,
            place,
            description,
            done
        )
    ).map { id ->
        NBBangHistory(
            id,
            date,
            peopleCount,
            totalPrice,
            joinPeople,
            place,
            description,
            done
        )
    }

    override fun get(): Single<List<NBBangHistory>> = mNBBangDao.get().map {it.map(::convert)}

    override fun get(id: Long): Maybe<NBBangHistory> = mNBBangDao.get(id).map(::convert)

    override fun remove(id: Long) = mNBBangDao.delete(id)
}