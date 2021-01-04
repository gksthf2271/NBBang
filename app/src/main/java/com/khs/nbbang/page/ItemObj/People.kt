package com.khs.nbbang.page.ItemObj

class People {
    var mName: String = ""
    var mIndex: Int = -1
    var mSelectedFlag : Boolean = false

    constructor(index:Int, name:String){
        mName = name
        mIndex = index
    }

    constructor(name:String) {
        mName = name
    }
}