package com.khs.nbbang.history.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface NBBangDao {
    @Query("SELECT * FROM nbbang")
    fun get(): Single<List<NBBangDataModel>>

    @Query("SELECT * FROM nbbang WHERE id = :id")
    fun get(id: Long): Maybe<NBBangDataModel>

    @Insert
    fun insert(nbb: NBBangDataModel): Single<Long>

    @Query("DELETE from nbbang WHERE id = :id")
    fun delete(id: Long)
}