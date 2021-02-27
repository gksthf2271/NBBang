package com.khs.nbbang.group

import com.khs.nbbang.page.ItemObj.JoinPeople

data class memberRequest(
    var id: Long = -1,
    var memberRequestIndex: Int = -1,
    var memberRequestName: String = "",
    var groupId: Long,
    var description: String,
    var memberRequestResId: Int
) : JoinPeople(memberRequestIndex, memberRequestName, memberRequestResId)