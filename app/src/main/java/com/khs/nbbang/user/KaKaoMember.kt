package com.khs.nbbang.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class KaKaoMember (
    var id: Long = -1,
    var index: Int = -1,
    var profileNickname: String = "",
    var groupId: Long = -1,
    var description: String = "",
    var uuId: String = "",
    var thumbnailImage : String? = null,
    var isFavorite: Boolean? = null,
    var isFavoriteByKakao: Boolean? = null
)

/*
@Parcelize
data class Friend(
    val id: Long,
    val uuid: String,
    val profileNickname: String,
    val profileThumbnailImage: String,
    val favorite: Boolean
) : Parcelable
*/
