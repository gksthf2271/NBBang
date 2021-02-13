package com.khs.nbbang.history.db

import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.ItemObj.Place

data class NBBangHistory(val id : Long,
                         val date: Long,
                         val peopleCount: Int,
                         val totalPrice: Int,
                         val joinPeople: List<People>,
                         val place: List<Place>,
                         val description: String,
                         val done: Boolean)