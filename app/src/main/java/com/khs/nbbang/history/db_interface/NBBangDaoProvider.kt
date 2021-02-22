package com.khs.nbbang.history.db_interface

import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBPlaceDao

interface NBBangDaoProvider  {
    val mNBBPlaceDao : NBBPlaceDao
    val mNBBMemberDao : NBBMemberDao
}