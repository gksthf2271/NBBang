package com.khs.nbbang.group

import com.khs.nbbang.history.db_interface.NBBangGateway
import com.khs.nbbang.history.room.NBBMemberDataModel
import com.khs.nbbang.user.Member
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
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
    fun addNBBangMember(request: memberRequest): Single<Member> = addMember(
        request.name,
        request.groupId,
        request.description,
        request.resId
    )
}

interface DeleteNBBangMember : NBBangGateway {
    fun deleteNBBangMember(request: memberRequest): Single<Int> = removeMember(
        request.id
    )
}

interface UpdateNBBangMember : NBBangGateway {
//    fun updateNBBangMember(request: memberRequest): Maybe<NBBMemberDataModel> = updateMember(
//        Member(
//            request.id,
//            request.name,
//            request.groupId,
//            request.description,
//            request.resId
//        )
//    )
}
