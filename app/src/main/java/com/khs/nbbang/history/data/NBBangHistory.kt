package com.khs.nbbang.history.data

data class NBBangHistory(val id : Long,
                         val date: Long,
                         val peopleCount: Int,
                         val totalPrice: Int,
                         val joinPeople: String,
                         val place: String,
                         val description: String,
                         val done: Boolean)