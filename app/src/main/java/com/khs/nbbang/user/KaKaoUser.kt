package com.khs.nbbang.user

import com.kakao.sdk.user.model.Account
import java.util.*

data class KaKaoUser(override var id : Long,
                     var properties: Map<String, String>? = null,
                     var kakaoAccount: Account? = null,
                     var groupUserToken: String? = null,
                     var connectedAt: Date? = null,
                     var synchedAt: Date? = null) : User(id)