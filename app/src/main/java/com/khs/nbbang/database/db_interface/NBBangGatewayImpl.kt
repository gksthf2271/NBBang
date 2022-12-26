package com.khs.nbbang.database.db_interface

import com.khs.nbbang.common.MemberType
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.NBBangHistory
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.database.room.NBBMemberDataModel
import com.khs.nbbang.database.room.NBBHistoryDataModel
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface NBBangGatewayImpl : NBBangGateway, NBBangDaoProvider {
    private fun convert(d: NBBHistoryDataModel): NBBangHistory =
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
    ): Single<NBBangHistory> = mNBBHistoryDao.insert(
        NBBHistoryDataModel(
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

    override fun get(): Single<List<NBBangHistory>> = mNBBHistoryDao.get().map { it.map(::convert) }

    override fun get(id: Long): Maybe<NBBangHistory> = mNBBHistoryDao.get(id).map(::convert)

    override fun get(minTimeMs: Long, maxTimeMs: Long): Single<List<NBBangHistory>> = mNBBHistoryDao.get(minTimeMs, maxTimeMs).map { it.map(::convert) }

    override fun remove(id: Long) = mNBBHistoryDao.delete(id)

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

    override fun addMember(
        name: String,
        index: Int,
        groupId: Long,
        description: String,
        kakaoId: String,
        thumbnailImage: String?,
        profileImage: String?,
        profileUri: String?,
        isFavorite: Int?,
        isFavoriteByKakao: Int?
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


    override fun removeMember(id: Long): Single<Int> = mNBBMemberDao.delete(id)

    override fun removeMemberByType(memberType: MemberType): Single<Int> =
        when (memberType) {
            MemberType.TYPE_KAKAO -> {
                mNBBMemberDao.deleteKakaoMembers("")
            }
            MemberType.TYPE_FREE_USER -> {
                mNBBMemberDao.deleteLocalMembers("")
            }
        }


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