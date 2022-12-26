package com.khs.nbbang.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.khs.nbbang.history.data.DutchPayPeople
import com.khs.nbbang.history.data.Place

@Entity(tableName = "nbb_history")
data class NBBHistoryDataModel(
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

    @ColumnInfo(name = "index")
    var index: Int,

    @ColumnInfo(name = "groupId")
    var groupId: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "kakao_id")
    var kakaoId: String,

    @ColumnInfo(name = "thumbnail_image")
    var thumbnailImage : String?,

    @ColumnInfo(name = "profile_image")
    var profileImage : String?,

    @ColumnInfo(name = "profile_uri")
    var profileUri: String?,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Int?,

    @ColumnInfo(name = "isFavoriteByKakao")
    var isFavoriteByKakao: Int?
)

@Entity(tableName = "nbb_keywords")
data class NBBSearchKeywordDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,

    @ColumnInfo(name = "keyword")
    var keyword: String,

    @ColumnInfo(name = "search_count")
    var searchCount: Int?
)


//카카오 API 검색 thumbNail url
//<span class="bg_present" style="background-image:url('//t1.kakaocdn.net/thumb/T800x0.q80/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F9675B66506254FB99A9E8A23AED80ABD')"></span>

@Entity(tableName = "nbb_place")
data class NBBPlaceDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "group_code")
    var groupCode: String,

    @ColumnInfo(name = "group_name")
    var groupName: String,

    @ColumnInfo(name = "category_name")
    var categoryName: String,

    @ColumnInfo(name = "phone")
    var phone: String,

    @ColumnInfo(name = "addressName")
    var addressName: String,

    @ColumnInfo(name = "place_url")
    var placeUrl: String,

    @ColumnInfo(name = "roadAddressName")
    var roadAddressName: String,

    @ColumnInfo(name = "x")
    var position_x: String,

    @ColumnInfo(name = "y")
    var position_y: String,

    @ColumnInfo(name = "thumbNailUrl")
    var thumbNailUrl: String,
)