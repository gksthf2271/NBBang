package com.khs.nbbang.group

import com.khs.nbbang.history.db_interface.NBBangGateway
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Single

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
    fun addNBBangMember(request: AddMemberRequest): Single<Member> = addMember(
        request.name,
        request.groupId,
        request.description,
        request.resId
    )
}
