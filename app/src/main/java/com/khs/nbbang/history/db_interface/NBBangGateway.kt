package com.khs.nbbang.history.db_interface

import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

/**
 * Gateway의 역할
 *  - UseCase가 DB에 직접적인 접근을 막고자 사용됨.
 */

interface NBBangGateway {

    fun get() : Single<List<NBBangHistory>>
    fun get(id: Long) : Maybe<NBBangHistory>
    fun get(minTimeMs: Long, maxTimeMs: Long) : Single<List<NBBangHistory>>
    fun remove(id: Long)
    fun add(
        date: Long,
        place: List<Place>,
        dutchPay: List<DutchPayPeople>,
        description: String
    ): Single<NBBangHistory>



    fun getMembers() : Single<List<Member>>
    fun getMember(id: Long) : Maybe<Member>
    fun getMemberByGroupId(groupId: Long) : Single<List<Member>>
    fun removeMember(id: Long)
    fun addMember(
        name : String,
        groupId: Long,
        description: String,
        resId: Int
    ): Single<Member>
}