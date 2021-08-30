package com.khs.nbbang.utils

import java.text.DecimalFormat

class NumberUtils {
    fun makeCommaNumber(input:Int): String{
        return makeCommaNumber(false, input)
    }

    fun makeCommaNumber(isMoney : Boolean, input:Int): String{
        val formatter = DecimalFormat("###,###")
        return if (isMoney) "${formatter.format(input)} 원"
        else formatter.format(input)
    }
}