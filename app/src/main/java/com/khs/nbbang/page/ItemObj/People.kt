package com.khs.nbbang.page.ItemObj

class People {
    var mName: String = ""
    var mIndex: Int = -1
    var mNNBMoney: Int = 0
    //Map<장소 아이디, 지불 가격>
    var mJoinPlaceMap: MutableMap<Int, Int> = mutableMapOf()

    constructor(index:Int, name:String){
        mName = name
        mIndex = index
    }

    constructor(name:String) {
        mName = name
    }
}