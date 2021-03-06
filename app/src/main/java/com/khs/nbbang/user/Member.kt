package com.khs.nbbang.user

import android.net.Uri

data class Member(
    var id: Long = -1,
    var index: Int = -1,
    var name: String = "",
    var groupId: Long = -1,
    var description: String = "",
    var kakaoId: Long = -1,
    var thumbnailImage : String? = null,
    var profileImage : String? = null,
    var profileUri: String? = null
)
