package com.khs.nbbang.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.page.ItemObj.NBB
import com.khs.nbbang.user.Member
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
    fun stringToDutchPayPeopleList(value: String?): List<DutchPayPeople>? {
        val listType: Type =
            object : TypeToken<List<DutchPayPeople>?>() {}.type
        return Gson().fromJson(
            value,
            listType
        )
    }

    @androidx.room.TypeConverter
    fun dutchPayPeopleListToString(list: List<DutchPayPeople>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @androidx.room.TypeConverter
    fun stringToPlaceList(value: String?): List<Place>? {
        val listType: Type =
            object : TypeToken<List<Place>?>() {}.type
        return Gson().fromJson(
            value,
            listType
        )
    }

    @androidx.room.TypeConverter
    fun placeListToString(list: List<Place>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @androidx.room.TypeConverter
    fun stringToJoinPeopleList(value: String?): List<Member>? {
        val listType: Type =
            object : TypeToken<List<NBB>?>() {}.type
        return Gson().fromJson(
            value,
            listType
        )
    }

    @androidx.room.TypeConverter
    fun joinPeopleListToString(list: List<Member>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}
