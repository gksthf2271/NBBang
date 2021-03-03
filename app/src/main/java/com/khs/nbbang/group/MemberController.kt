package com.khs.nbbang.group

import com.khs.nbbang.user.Member

interface MemberController {
    fun requestAddMember(
        member: Member
    ): memberRequest =
        memberRequest(
            member.id,
            member.index,
            member.name,
            member.groupId,
            member.description,
            member.kakaoId,
            member.thumbnailImage,
            member.profileImage,
            member.profileUri
        )



    fun requestUpdateMember(
        targetMember : Member,
        updateMember : Member
    ): memberRequest =
        memberRequest(
            targetMember.id,
            targetMember.index,
            updateMember.name,
            updateMember.groupId,
            updateMember.description,
            updateMember.kakaoId,
            updateMember.thumbnailImage,
            updateMember.profileImage,
            updateMember.profileUri
        )
}