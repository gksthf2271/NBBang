package com.khs.nbbang.history.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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

    @Query("SELECT * FROM nbb_member WHERE kakao_id != :empty ")
    fun getKakaoFriends(empty : String): Single<List<NBBMemberDataModel>>

    @Query("SELECT * FROM nbb_member WHERE kakao_id == :empty")
    fun getLocalFriends(empty : String): Single<List<NBBMemberDataModel>>

    @Query("SELECT * FROM nbb_member")
    fun getAllFriends(): Single<List<NBBMemberDataModel>>

    @Insert
    fun insert(nbb: NBBMemberDataModel): Single<Long>

    @Query("DELETE from nbb_member WHERE id = :id")
    fun delete(id: Long) : Single<Int>

    @Query("DELETE from nbb_member WHERE kakao_id != :empty")
    fun deleteKakaoMembers(empty: String) : Single<Int>

    @Query("DELETE from nbb_member WHERE kakao_id == :empty")
    fun deleteLocalMembers(empty: String) : Single<Int>

    @Delete
    fun delete(member:NBBMemberDataModel) : Completable

//    @Update
//    fun update(member: Member) : Maybe<NBBMemberDataModel>

    @Query("UPDATE nbb_member SET name = :updateName, description = :updateDescription, kakao_id = :updateKakaoId, thumbnail_image = :updateThumbnailImage, profile_image = :updateProfileImage, profile_uri = :updateProfileUri WHERE id == :targetId")
    fun update(targetId : Long, updateName : String, updateDescription : String, updateKakaoId : String, updateThumbnailImage: String?, updateProfileImage: String?, updateProfileUri: String?) : Single<Int>
}

@Dao
interface NBBSearchKeywordsDao {
    @Query("SELECT * From nbb_keywords")
    fun getKeywords() : Single<List<NBBSearchKeywordDataModel>>

    @Query("SELECT * From nbb_keywords WHERE keyword == :keyword")
    fun getKeywordCount(keyword: String) : Single<NBBSearchKeywordDataModel>

    @Insert
    fun insert(nbb: NBBSearchKeywordDataModel): Single<Long>

    @Query("DELETE from nbb_keywords WHERE keyword = :keyword")
    fun delete(keyword: String) : Completable

    @Query("DELETE from nbb_keywords")
    fun deleteAll() : Completable

    @Query("UPDATE nbb_keywords SET search_count = :searchCount WHERE keyword == :keyword")
    fun update(keyword: String, searchCount: Int) : Single<Int>
}