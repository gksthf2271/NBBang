package com.khs.nbbang.group

import com.khs.nbbang.user.User

data class AddMemberRequest(
    override var id: Long,
    override var name: String,
    var groupId: Long,
    var description: String,
    var resId: Int
) : User(id, name)