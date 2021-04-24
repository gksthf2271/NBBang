package com.khs.nbbang.history.db_interface

import android.net.Uri
import com.khs.nbbang.common.MemberType
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.history.room.NBBMemberDataModel
import com.khs.nbbang.history.room.NBBPlaceDataModel
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface NBBangGatewayImpl : NBBangGateway, NBBangDaoProvider {
    private fun convert(d: NBBPlaceDataModel): NBBangHistory =
        NBBangHistory(
            d.id!!,
            d.date,
            d.place,
            d.dutchPay,
            d.description
        )

    override fun add(
        date: Long,
        place: List<Place>,
        dutchPay: List<DutchPayPeople>,
        description: String
    ): Single<NBBangHistory> = mNBBPlaceDao.insert(
        NBBPlaceDataModel(
            null,
            date,
            place,
            dutchPay,
            description
        )
    ).map { id ->
        NBBangHistory(
            id,
            date,
            place,
            dutchPay,
            description
        )
    }

    override fun get(): Single<List<NBBangHistory>> = mNBBPlaceDao.get().map { it.map(::convert) }

    override fun get(id: Long): Maybe<NBBangHistory> = mNBBPlaceDao.get(id).map(::convert)

    override fun get(minDate: Long, maxDate: Long): Single<List<NBBangHistory>> =
        mNBBPlaceDao.get(minDate, maxDate).map { it.map(::convert) }

    override fun remove(id: Long) = mNBBPlaceDao.delete(id)

    private fun convertMember(d: NBBMemberDataModel): Member =
        Member(
            d.id!!,
            d.index,
            d.name,
            d.groupId,
            d.description,
            d.kakaoId,
            d.thumbnailImage,
            d.profileImage,
            d.profileUri,
            d.isFavorite,
            d.isFavoriteByKakao
        )

    override fun addMember(name : String,
                           index : Int,
                           groupId: Long,
                           description: String,
                           kakaoId: String,
                           thumbnailImage: String?,
                           profileImage: String?,
                           profileUri: String?,
                           isFavorite: Int?,
                           isFavoriteByKakao : Int?
    ): Single<Member> =
        mNBBMemberDao.insert(
            NBBMemberDataModel(
                null,
                0,
                0,
                name,
                description,
                kakaoId,
                thumbnailImage,
                profileImage,
                profileUri,
                isFavorite,
                isFavoriteByKakao
            )
        ).map { id ->
            Member(
                id,
                index,
                name,
                groupId,
                description,
                kakaoId,
                thumbnailImage,
                profileImage,
                profileUri
            )
        }

    override fun getMembers(): Single<List<Member>> =
        mNBBMemberDao.get().map { it.map(::convertMember) }

    override fun getMember(id: Long): Maybe<Member> =
        mNBBMemberDao.get(id).map(::convertMember)

    override fun getMemberByGroupId(groupId: Long): Single<List<Member>> =
        mNBBMemberDao.getByGroupId(groupId).map { it.map(::convertMember) }

    override fun getMemberByType(type: MemberType): Single<List<Member>> =
        when (type) {
            MemberType.TYPE_FREE_USER -> {
                mNBBMemberDao.getLocalFriends("").map { it.map(::convertMember) }
            }
            MemberType.TYPE_KAKAO -> {
                mNBBMemberDao.getKakaoFriends("").map { it.map(::convertMember) }
            }
        }



    override fun removeMember(id: Long) : Single<Int> = mNBBMemberDao.delete(id)

    override fun updateMember(member: Member): Single<Int> = mNBBMemberDao.update(
        member.id,
        member.name,
        member.description,
        member.kakaoId,
        member.thumbnailImage,
        member.profileImage,
        member.profileUri
    )
}