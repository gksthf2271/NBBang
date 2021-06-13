package com.khs.nbbang.history.data

import com.khs.nbbang.user.Member
import java.io.Serializable

data class NBBResultItem(
    val place: ArrayList<Place>,
    val dutchPay: ArrayList<DutchPayPeople>
) : Serializable

data class Place(
    val placeIndex: Int,
    val joinPeopleCount: Int,
    val placeName: String,
    val joinPeopleList: ArrayList<Member>,
    val price: Int,
    val dutchPay: Long
) : Serializable

data class DutchPayPeople(
    val dutchPayPeopleIndex: Long,
    val dutchPayPeopleName: String,
    val dutchPay: Long
) : Serializable