package com.khs.nbbang.kakaoFriends

data class KakaoRequest (
    var id: Long = -1,
    var index: Int = -1,
    var profileNickname: String = "",
    var groupId: Long = -1,
    var description: String = "",
    var uuId: Long = -1,
    var thumbnailImage : String? = null,
    var isFavorite: Boolean? = null,
    var isFavoriteByKakao: Boolean? = null
)