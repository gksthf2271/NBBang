package com.khs.nbbang.utils

import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.user.Member

object StringUtils {
    val TAG_CLASS = this.javaClass.simpleName

    fun getPeopleList(list : MutableList<Member>) : String{
        var peopleNameString = ""
        for(people in list) {
            LogUtil.vLog(null, TAG_CLASS, "peopleName : ${people.name}")
            peopleNameString += people.name
            if (people != list.lastOrNull()) {
                peopleNameString += ", "
            }
        }
        return peopleNameString
    }

    fun listToString(list : MutableList<String>) : String{
        var result = ""
        for(str in list) {
            result += str
            if (str != list.lastOrNull()) {
                result += ", "
            }
        }
        return result
    }

    fun <T> listToAny(list : MutableList<T>) : String {
        var result = ""
        for(item in list) {
            result += item.toString()
            if (item.toString() != list.lastOrNull()) {
                result += ", "
            }
        }
        return result
    }

    fun dutchPayListToString(list : MutableList<DutchPayPeople>) : String{
        var result = ""
        for(dutchPayPeople in list) {
            result += dutchPayPeople.dutchPayPeopleName
            if (dutchPayPeople != list.lastOrNull()) {
                result += ", "
            }
        }
        return result
    }
}