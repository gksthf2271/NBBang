package com.khs.nbbang.kakaoFriends.kakao_interface

data class ReturnType (
    val RETURN_TYPE_NONE_SUCCESS : Int = 100,
    val RETURN_TYPE_PROFILE_SUCCESS : Int = 101,
    val RETURN_TYPE_LOGIN_SUCCESS : Int = 102,
    val RETURN_TYPE_LOGOUT_SUCCESS : Int = 103,
    val RETURN_TYPE_MY_INFO_SUCCESS : Int = 104,
    val RETURN_TYPE_NONE_FAILED : Int = 200,
    val RETURN_TYPE_LOGIN_FAILED : Int = 201
)