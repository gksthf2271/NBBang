package com.khs.nbbang.history.db

import com.khs.nbbang.history.room.NBBPlaceDao

interface NBBangDaoProvider  {
    val mNBBPlaceDao : NBBPlaceDao
}