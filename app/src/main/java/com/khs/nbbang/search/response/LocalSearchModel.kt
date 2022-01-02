package com.khs.nbbang.search.response

import com.google.gson.annotations.SerializedName

data class LocalSearchModel(
    @SerializedName("meta")
    val meta: MetaModel,
    @SerializedName("documents")
    val documents: List<DocumnetModel>
)

data class DocumnetModel(
    @SerializedName("id")
    val id : String,                // 장소 ID

    @SerializedName("place_name")
    val place_name : String,        // 장소명, 업체명

    @SerializedName("category_name")
    val category_name : String,     // 카테고리 이름

    @SerializedName("category_group_code")
    val category_group_code : String,// 중요 카테고리만 그룹핑한 카테고리 그룹 코드

    @SerializedName("category_group_name")
    val category_group_name : String,// 중요 카테고리만 그룹핑한 카테고리 그룹명

    @SerializedName("phone")
    val phone : String,             // 전화번호

    @SerializedName("address_name")
    val address_name : String,      // 전체 지번 주소

    @SerializedName("road_address_name")
    val road_address_name : String, // 전체 도로명 주소

    @SerializedName("x")
    val x : String,                 // X 좌표값, 경위도인 경우 longitude (경도)

    @SerializedName("y")
    val y : String,                 // Y 좌표값, 경위도인 경우 latitude(위도)

    @SerializedName("place_url")
    val place_url : String,         // 장소 상세페이지 URL

    @SerializedName("distance")
    val distance : String           // 중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재), 단위 meter
)

data class MetaModel(
    @SerializedName("total_count")
    val total_count: Int,           // Integer	검색어에 검색된 문서 수

    @SerializedName("pageable_count")
    val pageable_count: Int,        // Integer	total_count 중 노출 가능 문서 수 (최대: 45)

    @SerializedName("is_end")
    val is_end: Boolean,            // 현재 페이지가 마지막 페이지인지 여부

    @SerializedName("same_name")
    val items: RegionInfoModel      // 질의어의 지역 및 키워드 분석 정보

)

data class RegionInfoModel(
    @SerializedName("region")
    val region: List<String>,           // 질의어에서 인식된 지역의 리스트

    @SerializedName("keyword")
    val keyword: String,            // 질의어에서 지역 정보를 제외한 키워드

    @SerializedName("selected_region")
    val selected_region: String     // 인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보
)