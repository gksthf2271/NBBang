package com.khs.nbbang.kakaoUser

import com.kakao.sdk.user.model.Account
import java.util.*

data class KaKaoUser(var id: Long = -1,
                     var properties: Map<String, String>? = null,
                     var kakaoAccount: Account? = null,
                     var groupUserToken: String? = null,
                     var connectedAt: Date? = null,
                     var synchedAt: Date? = null)