package com.khs.nbbang.utils

import android.util.Log
import com.khs.nbbang.page.ItemObj.People

class StringUtils() {
    val TAG = this.javaClass.name

    fun getPeopleList(list : MutableList<People>) : String{
        var peopleNameString = ""
        return peopleNameString.apply {
            for(people in list) {
                Log.v(TAG,"peopleName : ${people.mName}")
                peopleNameString += people.mName
                if (people != list.lastOrNull()) {
                    peopleNameString += ", "
                }
            }
        }
    }
}