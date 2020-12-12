package com.anufriev.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anufriev.data.db.CityDatabase.Companion.DATABASE_VERSION
import com.anufriev.data.db.converter.*

//@Database(
//    entities = [],
//    version = DATABASE_VERSION
//)
@TypeConverters(
    ListDoubleConverter::class,
    ListIntConverter::class
)
abstract class CityDatabase : RoomDatabase() {

    companion object {
        lateinit var instance: CityDatabase
            private set
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CityDB"
        fun buildDataSource(context: Context): CityDatabase {
            val room = Room.databaseBuilder(context, CityDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
            instance = room
            return room
        }
    }
}