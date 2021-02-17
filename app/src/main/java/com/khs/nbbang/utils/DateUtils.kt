package com.khs.nbbang.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

open class DateUtils {
    constructor()

    private object TIME_MAXIMUM {
        val ONE_MIN = 60 * 1000
        val ONE_HOUR = 60 * ONE_MIN
        val ONE_DAY = 24 * ONE_HOUR
        val ONE_MONTH = 30 * ONE_DAY
        val ONE_YEARS = 12 * ONE_MONTH
    }

    open fun formatTimeString(regTime: Long): String {
        val curTime = System.currentTimeMillis()
        var diffTime = (curTime - regTime)

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

    open fun currentMonth() : Int {
        return Calendar.getInstance().get(Calendar.MONTH).plus(1)
    }

    open fun currentYear() : Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    open fun getDateByString(date: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss")
        val result = simpleDateFormat.parse(date.replace("T","-"))
        return result.time
    }

    open fun getDateforImg(date: Long): String {
        var simpleDateFormat: SimpleDateFormat
        var result: String
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

    open fun getDateByMillis(timeMs:Long) : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val timeInDate = Date(timeMs)
        return sdf.format(timeInDate)
    }

    open fun getTimeMsByYearAndMonth(year: Int, month: Int) : Long{
        val format = "${year}-${String.format("%02d", month)}-01 00:00:00.000"
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return dateFormat.parse(format).time
    }

    open fun getTimeMsByMonth(month: Int) : Long{
        return getTimeMsByYearAndMonth(currentYear(),month)
    }

    open fun getDateByMonth(year:Int, month: Int) {
        val dateFormat = SimpleDateFormat(String.format("%02d", month))
    }
}