package com.khs.nbbang.freeUser

import com.khs.nbbang.page.ItemObj.People

interface SelectPeopleCallback {
    fun onCallback(people: People, isSelect:Boolean)
}