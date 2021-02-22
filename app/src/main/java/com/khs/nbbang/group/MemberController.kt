package com.khs.nbbang.group

import com.khs.nbbang.user.Member

interface MemberController {
    fun requestAddMember(
        member: Member
    ): AddMemberRequest =
        AddMemberRequest(
            member.id,
            member.name,
            member.groupId,
            member.description,
            member.resId
        )
}