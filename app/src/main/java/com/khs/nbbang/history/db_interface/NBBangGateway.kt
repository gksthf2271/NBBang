package com.khs.nbbang.history.db_interface

import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.page.ItemObj.People
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

/**
 * Gateway의 역할
 *  - UseCase가 DB에 직접적인 접근을 막고자 사용됨.
 */

interface NBBangGateway {
//
//    interface NBBDataSource {
//        interface GetPostsCallback {
//            fun onNBBsLoaded(nbbs: List<NBBangHistory>)
//            fun onError(t: Throwable)
//        }
//
//        interface SaveTaskCallback {
//            fun onSaveSuccess()
//            fun onError(t: Throwable)
//        }
//
//        fun get(id: Long, callback: GetPostsCallback) : Maybe<NBBangHistory>
//        fun savePost(nbb: NBBangHistory) : Single<NBBangHistory>
//    }

    fun get() : Single<List<NBBangHistory>>
    fun get(id: Long) : Maybe<NBBangHistory>
    fun remove(id: Long)
    fun add(
        date: Long,
        peopleCount: Int,
        totalPrice: Int,
        joinPeople: List<People>,
        description: String
    ): Single<NBBangHistory>
}