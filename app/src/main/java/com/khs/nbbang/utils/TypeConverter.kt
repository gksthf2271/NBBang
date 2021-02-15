package com.khs.nbbang.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.page.ItemObj.People
import java.lang.reflect.Type

class TypeConverter {
    @androidx.room.TypeConverter
    fun fromString(value: String?): List<NBB>? {
        val listType: Type =
            object : TypeToken<List<NBB>?>() {}.type
        return Gson().fromJson(
            value,
            listType
        )
    }

    @androidx.room.TypeConverter
    fun listToString(list: List<NBB>?): String? {
        val gson = Gson()
        return gson.toJson(list)

    }

    @androidx.room.TypeConverter
    fun stringToPeopleList(value: String?): List<People>? {
        val listType: Type =
            object : TypeToken<List<NBB>?>() {}.type
        return Gson().fromJson(
            value,
            listType
        )
    }

    @androidx.room.TypeConverter
    fun peopleListToString(list: List<People>?): String? {
        val gson = Gson()
        return gson.toJson(list)

    }
}
