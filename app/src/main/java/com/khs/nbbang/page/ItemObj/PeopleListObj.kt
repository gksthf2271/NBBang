package com.khs.nbbang.page.ItemObj

class PeopleListObj(){
    fun PeopleListObj(peopleCount : Int) {
        mPeopleList = mutableListOf()
        mPeopleCount = peopleCount
    }

    fun PeopleListObj(peopleList: MutableList<People>) {
        mPeopleList = peopleList
        mPeopleCount = peopleList.size
    }

    var mPeopleList : MutableList<People> = mutableListOf()
    var mPeopleCount : Int = 0
}