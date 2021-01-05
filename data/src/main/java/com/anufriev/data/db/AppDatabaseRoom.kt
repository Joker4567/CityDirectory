package com.anufriev.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anufriev.data.db.CityDatabase.Companion.DATABASE_VERSION
import com.anufriev.data.db.converter.*
import com.anufriev.data.db.dao.FeedBackDao
import com.anufriev.data.db.dao.OrganizationDao
import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity

@Database(
    entities = [
        OrganizationDaoEntity::class,
        FeedBackDaoEntity::class],
    version = DATABASE_VERSION
)
@TypeConverters(
    ListDoubleConverter::class,
    ListIntConverter::class
)
abstract class CityDatabase : RoomDatabase() {

    abstract fun getOrg(): OrganizationDao
    abstract fun getFeedBack() : FeedBackDao
    companion object {
        lateinit var instance: CityDatabase
            private set
        const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "CityDB"
        fun buildDataSource(context: Context): CityDatabase {
            val room = Room.databaseBuilder(context, CityDatabase::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build()
            instance = room
            return room
        }
    }
}