package com.khs.nbbang.user

import com.khs.nbbang.page.ItemObj.JoinPeople

data class Member(
    var id: Long = -1,
    var memberIndex: Int = -1,
    var memberName: String = "",
    var groupId: Long,
    var description: String,
    var memberResId: Int
) : JoinPeople(memberIndex, memberName, memberResId)
