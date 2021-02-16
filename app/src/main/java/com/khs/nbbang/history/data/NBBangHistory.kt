package com.khs.nbbang.history.data

data class NBBangHistory(val id : Long?,
                         val date: Long,
                         val place: List<Place>,
                         val dutchPay: List<DutchPayPeople>,
                         val description: String)