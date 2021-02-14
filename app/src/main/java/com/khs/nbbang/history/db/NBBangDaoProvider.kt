package com.khs.nbbang.history.db

import com.khs.nbbang.history.room.NBBangDao

interface NBBangDaoProvider  {
    val mNBBangDao : NBBangDao
}