package com.khs.nbbang.history.data

import com.khs.nbbang.page.ItemObj.People

data class NBBResultItem(
    val place: ArrayList<Place>,
    val dutchPay: ArrayList<DutchPayPeople>
)

data class Place(
    val placeIndex : Int,
    val joinPeopleCount : Int,
    val placeName : String,
    val peopleList : ArrayList<People>,
    val price : Int,
    val dutchPay : Long)

data class DutchPayPeople(
    val index : Long,
    val name : String,
    val dutchPay : Long
)