package com.khs.nbbang.history.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.khs.nbbang.utils.TypeConverter

@Database(entities = [NBBHistoryDataModel::class, NBBMemberDataModel::class, NBBSearchKeywordDataModel::class, NBBPlaceDataModel::class], version = 3)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun nbbangDao(): NBBHistoryDao
    abstract fun nbbMemberDao(): NBBMemberDao
    abstract fun nbbSearchKeywordDao() : NBBSearchKeywordsDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "nbb.db"
                    )
                        .addMigrations(MIGRATION_1_2).build()
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

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'nbb_place' RENAME TO 'nbb_history'")
//        database.execSQL("ALTER TABLE 'nbb_member' ADD 'isFavorite' INTEGER DEFAULT 0 ")
//        database.execSQL("ALTER TABLE 'nbb_member' ADD 'isFavoriteByKakao' INTEGER DEFAULT 0 ")

//        ALTER TABLE BLG_PJT_R_RISK ADD DEL_YN CHAR(1) NOT NULL DEFAULT 'N'
//
    }
}