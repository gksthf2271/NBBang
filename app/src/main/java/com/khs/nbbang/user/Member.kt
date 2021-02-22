package com.khs.nbbang.user

data class Member(override var id: Long, override var name: String, var groupId : Long, var description: String, var resId : Int) : User(id, name)
