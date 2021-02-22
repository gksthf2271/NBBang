package com.khs.nbbang.history.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.Place
import com.khs.nbbang.page.ItemObj.NBB

@Entity(tableName = "nbb_place")
data class NBBPlaceDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,

    @ColumnInfo(name = "date")
    var date: Long,

    @ColumnInfo(name = "place")
    var place: List<Place>,

    @ColumnInfo(name = "dutchPay")
    var dutchPay : List<DutchPayPeople>,

    @ColumnInfo(name = "description")
    var description: String
)

@Entity(tableName = "nbb_member")
data class NBBMemberDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,

    @ColumnInfo(name = "groupId")
    var groupId: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "resId")
    var resId: Int
)