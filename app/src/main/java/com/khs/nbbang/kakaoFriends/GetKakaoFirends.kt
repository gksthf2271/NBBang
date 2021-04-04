package com.khs.nbbang.kakaoFriends

import com.khs.nbbang.user.KaKaoMember
import com.khs.nbbang.user.Member

data class GetKakaoFirends (
    val kakaoFirends: List<KaKaoMember>)