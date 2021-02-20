package com.khs.nbbang.user

data class FreeUser(override var id : Long, override var name : String = "FREE USER", var imgRes : Int) : User(id, name)