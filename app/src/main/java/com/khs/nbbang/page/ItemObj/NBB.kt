package com.khs.nbbang.page.ItemObj

data class NBB(
    var mPeopleList: MutableList<People> = mutableListOf(),
    var mPeopleCount: Int = 0,
    var mPrice: String = "0",
    var mPlaceName: String = ""
)