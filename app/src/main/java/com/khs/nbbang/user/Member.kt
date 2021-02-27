package com.khs.nbbang.user

data class Member(
    var id: Long = -1,
    var index: Int = -1,
    var name: String = "",
    var groupId: Long,
    var description: String,
    var resId: Int
)
