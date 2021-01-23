package com.anufriev.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anufriev.data.db.CityDatabase.Companion.DATABASE_VERSION
import com.anufriev.data.db.converter.ListDoubleConverter
import com.anufriev.data.db.converter.ListIntConverter
import com.anufriev.data.db.dao.FeedBackDao
import com.anufriev.data.db.dao.FellowDao
import com.anufriev.data.db.dao.OrganizationDao
import com.anufriev.data.db.dao.PhoneCallDao
import com.anufriev.data.db.entities.FeedBackDaoEntity
import com.anufriev.data.db.entities.FellowDaoEntity
import com.anufriev.data.db.entities.OrganizationDaoEntity
import com.anufriev.data.db.entities.PhoneCallDaoEntity

@Database(
    entities = [
        OrganizationDaoEntity::class,
        FeedBackDaoEntity::class,
        FellowDaoEntity::class,
        PhoneCallDaoEntity::class],
    version = DATABASE_VERSION
)
@TypeConverters(
    ListDoubleConverter::class,
    ListIntConverter::class
)
abstract class CityDatabase : RoomDatabase() {

    abstract fun getOrg(): OrganizationDao
    abstract fun getFeedBack() : FeedBackDao
    abstract fun getFellow() : FellowDao
    abstract fun getPhoneCall() : PhoneCallDao

    companion object {
        lateinit var instance: CityDatabase
            private set
        const val DATABASE_VERSION = 5
        private const val DATABASE_NAME = "CityDB"
        fun buildDataSource(context: Context): CityDatabase {
            val room = Room.databaseBuilder(context, CityDatabase::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATE_3_4)
                .addMigrations(MIGRATE_4_5)
                .build()
            instance = room
            return room
        }
    }
}