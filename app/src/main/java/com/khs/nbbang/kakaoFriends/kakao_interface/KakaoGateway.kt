package com.khs.nbbang.kakaoFriends.kakao_interface

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface KakaoGateway {
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


    fun login(context: Context, callback: (token: OAuthToken?, error: Throwable?) -> Unit)
    fun getMembers() : Single<List<Member>>
    fun getMember(id: Long) : Maybe<Member>
    fun getMemberByGroupId(groupId: Long) : Single<List<Member>>
    fun removeMember(id: Long) : Single<Int>
    fun updateMember(member: Member) : Single<Int>
    fun addMember(
        name : String,
        index : Int,
        groupId: Long,
        description: String,
        kakaoId: String,
        thumbnailImage: String?,
        profileImage: String?,
        profileUri: String?
    ): Single<Member>
}