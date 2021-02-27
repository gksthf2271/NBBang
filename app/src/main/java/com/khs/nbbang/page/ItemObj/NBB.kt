package com.khs.nbbang.page.ItemObj

data class NBB(
    var mJoinPeopleList: MutableList<JoinPeople> = mutableListOf(),
    var mJoinPeopleCount: Int = 0,
    var mPrice: String = "0",
    var mPlaceName: String = ""
)