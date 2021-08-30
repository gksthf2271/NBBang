package com.khs.nbbang.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

open class DateUtils {
    constructor()

    private object TIME_MAXIMUM {
        const val ONE_MIN = 60 * 1000
        const val ONE_HOUR = 60 * ONE_MIN
        const val ONE_DAY = 24 * ONE_HOUR
        const val ONE_MONTH = 30 * ONE_DAY
        const val ONE_YEARS = 12 * ONE_MONTH
    }

    open fun formatTimeString(regTime: Long): String {
        val curTime = System.currentTimeMillis()
        val diffTime = (curTime - regTime)

        if (diffTime < TIME_MAXIMUM.ONE_MIN) {
            return "방금 전"
        }
        if ((diffTime / TIME_MAXIMUM.ONE_MIN.toLong()) < 60) {
            return (diffTime / TIME_MAXIMUM.ONE_MIN.toLong()).toString() + "분 전"
        }
        if ((diffTime / TIME_MAXIMUM.ONE_HOUR.toLong()) < 24) {
            return (diffTime / TIME_MAXIMUM.ONE_HOUR.toLong()).toString() + "시간 전"
        }
        return (diffTime / TIME_MAXIMUM.ONE_DAY.toLong()).toString() + "일 전"
    }

    fun currentMonth() : Int {
        return Calendar.getInstance().get(Calendar.MONTH).plus(1)
    }

    private fun currentYear() : Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getDateByString(date: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
        val result = simpleDateFormat.parse(date.replace("T","-"))
        return result.time
    }

    fun getDateforImg(date: Long): String {
        val simpleDateFormat: SimpleDateFormat
        val result: String
        try {
            simpleDateFormat = SimpleDateFormat("yyyy-MM-dd/HH:mm")
            result = simpleDateFormat.format(date)
        } catch (typeCastEx: TypeCastException) {
            return ""
        } catch (ex: Exception) {
            return ""
        }
        return result
    }

    fun getDateByMillis(timeMs:Long) : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val timeInDate = Date(timeMs)
        return sdf.format(timeInDate)
    }

    fun getTimeMsByYearAndMonth(year: Int, month: Int) : Long{
        val format = "${year}-${String.format("%02d", month)}-01 00:00:00.000"
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return dateFormat.parse(format).time
    }

    fun getTimeMsByMonth(month: Int) : Long{
        return getTimeMsByYearAndMonth(currentYear(),month)
    }

    open fun getDateByMonth(year:Int, month: Int) {
        val dateFormat = SimpleDateFormat(String.format("%02d", month))
    }
}