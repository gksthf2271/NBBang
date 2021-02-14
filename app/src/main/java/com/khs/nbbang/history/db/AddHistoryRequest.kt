package com.khs.nbbang.history.db

data class AddHistoryRequest(
    val date: Long,
    val peopleCount: Int,
    val totalPrice: Int,
    val joinPeople: String,
    val place: String,
    val description: String,
    val done: Boolean
)