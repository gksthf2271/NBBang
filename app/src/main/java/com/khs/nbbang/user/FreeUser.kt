package com.khs.nbbang.user

import com.khs.nbbang.R

data class FreeUser(
    override var id: Long,
    override var name: String = "FREE USER",
    var imgRes: Int = R.drawable.icon_user
) : User(id, name)