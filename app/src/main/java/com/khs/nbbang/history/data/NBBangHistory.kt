package com.khs.nbbang.history.data

import com.khs.nbbang.page.ItemObj.People

data class NBBangHistory(val id : Long?,
                         val date: Long,
                         val peopleCount: Int,
                         val totalPrice: Int,
                         val joinPeople: List<People>,
                         val description: String)