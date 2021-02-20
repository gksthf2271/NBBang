package com.khs.nbbang.user

data class FreeUser(override var id : Long, var name : String, var imgRes : Int) : User(id)