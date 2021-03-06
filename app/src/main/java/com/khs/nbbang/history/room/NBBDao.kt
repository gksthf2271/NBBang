package com.khs.nbbang.history.room

import android.net.Uri
import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface NBBPlaceDao {
    @Query("SELECT * FROM nbb_place")
    fun get(): Single<List<NBBPlaceDataModel>>

    @Query("SELECT * FROM nbb_place WHERE id = :id")
    fun get(id: Long): Maybe<NBBPlaceDataModel>

    @Query("SELECT * FROM nbb_place WHERE date BETWEEN :minDate AND :maxDate")
    fun get(minDate: Long, maxDate: Long): Single<List<NBBPlaceDataModel>>

    @Insert
    fun insert(nbb: NBBPlaceDataModel): Single<Long>

    @Query("DELETE from nbb_place WHERE id = :id")
    fun delete(id: Long)
}

@Dao
interface NBBMemberDao {
    @Query("SELECT * FROM nbb_member")
    fun get(): Single<List<NBBMemberDataModel>>

    @Query("SELECT * FROM nbb_member WHERE id = :id")
    fun get(id: Long): Maybe<NBBMemberDataModel>

    @Query("SELECT * FROM nbb_member WHERE groupId = :groupId")
    fun getByGroupId(groupId: Long): Single<List<NBBMemberDataModel>>

    @Insert
    fun insert(nbb: NBBMemberDataModel): Single<Long>

    @Query("DELETE from nbb_member WHERE id = :id")
    fun delete(id: Long) : Single<Int>

    @Delete
    fun delete(member:NBBMemberDataModel) : Completable

//    @Update
//    fun update(member: Member) : Maybe<NBBMemberDataModel>

    @Query("UPDATE nbb_member SET name = :updateName, description = :updateDescription, kakao_id = :updateKakaoId, thumbnail_image = :updateThumbnailImage, profile_image = :updateProfileImage, profile_uri = :updateProfileUri WHERE id == :targetId")
    fun update(targetId : Long, updateName : String, updateDescription : String, updateKakaoId : Long, updateThumbnailImage: String?, updateProfileImage: String?, updateProfileUri: String?) : Single<Int>
}