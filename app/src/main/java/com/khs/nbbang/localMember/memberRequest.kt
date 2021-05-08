package com.khs.nbbang.localMember

data class memberRequest(
    var id: Long = -1,
    var index: Int = -1,
    var name: String = "",
    var groupId: Long = -1,
    var description: String = "",
    var kakaoId: String = "",
    var thumbnailImage : String? = null,
    var profileImage : String? = null,
    var profileUri: String? = null,
    var isFavorite: Int? = null,
    var isFavoriteByKakao: Int? = null
)