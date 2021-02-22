package com.khs.nbbang.history.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.khs.nbbang.utils.TypeConverter

@Database(entities = [NBBPlaceDataModel::class, NBBMemberDataModel::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun nbbangDao(): NBBPlaceDao
    abstract fun nbbMemberDao(): NBBMemberDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    )
                        .also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "nbb.db"
            ).build()

    }
}