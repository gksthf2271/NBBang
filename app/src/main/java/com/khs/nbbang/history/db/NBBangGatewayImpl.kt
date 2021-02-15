package com.khs.nbbang.history.db

import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.room.NBBPlaceDataModel
import com.khs.nbbang.page.ItemObj.People
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface NBBangGatewayImpl : NBBangGateway, NBBangDaoProvider{
    private fun convert(d: NBBPlaceDataModel): NBBangHistory =
        NBBangHistory(
            d.id!!,
            d.date,
            d.peopleCount,
            d.totalPrice,
            d.joinPeople,
            d.description
        )
    override fun add(
        date: Long,
        peopleCount: Int,
        totalPrice: Int,
        joinPeople: List<People>,
        description: String
    ) : Single<NBBangHistory> = mNBBPlaceDao.insert(
        NBBPlaceDataModel(
            null,
            date,
            peopleCount,
            totalPrice,
            joinPeople,
            description
        )
    ).map { id ->
        NBBangHistory(
            id,
            date,
            peopleCount,
            totalPrice,
            joinPeople,
            description
        )
    }

    override fun get(): Single<List<NBBangHistory>> = mNBBPlaceDao.get().map {it.map(::convert)}

    override fun get(id: Long): Maybe<NBBangHistory> = mNBBPlaceDao.get(id).map(::convert)

    override fun remove(id: Long) = mNBBPlaceDao.delete(id)
}