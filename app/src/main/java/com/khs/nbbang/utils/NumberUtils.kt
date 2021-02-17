package com.khs.nbbang.utils

import java.text.DecimalFormat

class NumberUtils() {
    fun makeCommaNumber(input:Int): String{
        val formatter = DecimalFormat("###,###")
        return formatter.format(input)
    }
}