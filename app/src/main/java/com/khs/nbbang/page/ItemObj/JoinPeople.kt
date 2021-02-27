package com.khs.nbbang.page.ItemObj

open class JoinPeople(
    var index: Int = -1,
    var joinPeoplename: String = "",
    var joinPeoplenameResId: Int
) : People(joinPeoplename, joinPeoplenameResId)