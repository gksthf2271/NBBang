package com.khs.nbbang.database.db_interface

import com.khs.nbbang.database.room.NBBMemberDao
import com.khs.nbbang.database.room.NBBHistoryDao
import com.khs.nbbang.database.room.NBBPlaceDao
import com.khs.nbbang.database.room.NBBSearchKeywordsDao

interface NBBangDaoProvider  {
    val mNBBHistoryDao : NBBHistoryDao
    val mNBBPlaceDao : NBBPlaceDao
    val mNBBMemberDao : NBBMemberDao
    val mNBBKeywordsDao : NBBSearchKeywordsDao
}