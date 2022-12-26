package com.khs.nbbang.history.db_interface

import com.khs.nbbang.history.room.NBBMemberDao
import com.khs.nbbang.history.room.NBBHistoryDao
import com.khs.nbbang.history.room.NBBPlaceDao
import com.khs.nbbang.history.room.NBBSearchKeywordsDao

interface NBBangDaoProvider  {
    val mNBBHistoryDao : NBBHistoryDao
    val mNBBPlaceDao : NBBPlaceDao
    val mNBBMemberDao : NBBMemberDao
    val mNBBKeywordsDao : NBBSearchKeywordsDao
}