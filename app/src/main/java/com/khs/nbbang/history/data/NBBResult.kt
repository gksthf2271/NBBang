package com.khs.nbbang.history.data

import com.khs.nbbang.user.Member

data class NBBResultItem(
    val place: ArrayList<Place>,
    val dutchPay: ArrayList<DutchPayPeople>
)

data class Place(
    val placeIndex : Int,
    val joinPeopleCount : Int,
    val placeName : String,
    val joinPeopleList : ArrayList<Member>,
    val price : Int,
    val dutchPay : Long)

data class DutchPayPeople(
    val dutchPayPeopleIndex : Long,
    val dutchPayPeopleName : String,
    val dutchPay : Long
)