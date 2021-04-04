package com.khs.nbbang.kakaoFriends.kakao_interface

import com.khs.nbbang.group.GetNBBangMemberResult
import com.khs.nbbang.group.memberRequest
import com.khs.nbbang.history.db_interface.NBBangGateway
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Single

interface LoginKakao : KakaoGateway {

}

interface GetNbbangMember : NBBangGateway {
    fun getNBBangAllMember(): Single<GetNBBangMemberResult> = getMembers().map {
        GetNBBangMemberResult(it)
    }

    fun getNBBangAllMemberByGroupId(groupId: Long): Single<GetNBBangMemberResult> =
        getMemberByGroupId(groupId).map {
            GetNBBangMemberResult(it)
        }
}

interface AddNBBangMember : NBBangGateway {
    fun addNBBangMember(request: memberRequest): Single<Member> = addMember(
        request.name,
        request.index,
        request.groupId,
        request.description,
        request.kakaoId,
        request.thumbnailImage ?: "",
        request.profileImage ?: "",
        request.profileUri ?: ""
    )
}

interface DeleteNBBangMember : NBBangGateway {
    fun deleteNBBangMember(request: memberRequest): Single<Int> = removeMember(
        request.id
    )
}

interface UpdateNBBangMember : NBBangGateway {
    fun updateNBBangMember(request: memberRequest) : Single<Int> = updateMember(
        Member(
            request.id,
            request.index,
            request.name,
            request.groupId,
            request.description,
            request.kakaoId,
            request.thumbnailImage ?: "",
            request.profileImage ?: "",
            request.profileUri ?: ""
        )
    )
}