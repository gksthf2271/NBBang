package com.khs.nbbang.history.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khs.nbbang.page.ItemObj.People
import com.khs.nbbang.page.ItemObj.Place

@Entity(tableName = "nbbang")
data class NBBangDataModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var date: Long,
    var peopleCount : Int,
    var totalPrice : Int,

    /**
     * TODO
     * Room Type Cast 이슈
     * Json 형식 저장 하려면 파싱필요함.
     * 그전에 Data구조 확립해야됨.
     **/
    var joinPeople : String,
    var place: String,
    var description: String,
    var done: Boolean
)
