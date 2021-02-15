package com.khs.nbbang.history.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface NBBPlaceDao {
    @Query("SELECT * FROM nbb_place")
    fun get(): Single<List<NBBPlaceDataModel>>

    @Query("SELECT * FROM nbb_place WHERE id = :id")
    fun get(id: Long): Maybe<NBBPlaceDataModel>

    @Insert
    fun insert(nbb: NBBPlaceDataModel): Single<Long>

    @Query("DELETE from nbb_place WHERE id = :id")
    fun delete(id: Long)
}

@Dao
interface NBBPeopleDao {
    @Query("SELECT * FROM nbb_people")
    fun get(): Single<List<NBBPeopleDataModel>>

    @Query("SELECT * FROM nbb_people WHERE id = :id")
    fun get(id: Long): Maybe<NBBPeopleDataModel>

    @Insert
    fun insert(nbb: NBBPeopleDataModel): Single<Long>

    @Query("DELETE from nbb_people WHERE id = :id")
    fun delete(id: Long)
}