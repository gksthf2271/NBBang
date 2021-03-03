package com.khs.nbbang.group

import com.khs.nbbang.user.Member

interface MemberPresenter {

    fun presentMember(result: GetNBBangMemberResult): GetNBBangMemberResult =
        GetNBBangMemberResult(
            result.nbbangMemberList.map {
                Member(
                    it.id,
                    it.index,
                    it.name,
                    it.groupId,
                    it.description,
                    it.kakaoId,
                    it.thumbnailImage,
                    it.profileImage,
                    it.profileUri
                )
            }
        )

    companion object {
        private const val maxDescLength = 20
    }
}