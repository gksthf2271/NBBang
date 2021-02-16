package com.khs.nbbang.history.data

data class AddHistoryRequest(
    val date: Long,
    val place: List<Place>,
    val dutchPay : List<DutchPayPeople>,
    val description: String
)