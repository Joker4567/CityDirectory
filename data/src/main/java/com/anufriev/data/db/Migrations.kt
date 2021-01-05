package com.anufriev.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anufriev.data.db.contract.FeedBackContract
import com.anufriev.data.db.contract.OrganizationContract

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${OrganizationContract.tableName} ADD COLUMN `ratingGoodBad` TEXT NOT NULL DEFAULT '0/0'")
    }
}