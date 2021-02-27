package com.khs.nbbang.page.ItemObj

import com.khs.nbbang.user.Member

data class NBB(
    var mMemberList: MutableList<Member> = mutableListOf(),
    var mMemberCount: Int = 0,
    var mPrice: String = "0",
    var mPlaceName: String = ""
)