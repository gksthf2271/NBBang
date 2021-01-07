package com.khs.nbbang.page.ItemObj

class Place {
    var mName: String = ""
    var mIndex: Int = -1

    constructor(index:Int, name:String){
        mName = name
        mIndex = index
    }

    constructor(name:String) {
        mName = name
    }
}