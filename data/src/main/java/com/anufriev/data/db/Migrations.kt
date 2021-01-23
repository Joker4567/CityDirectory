package com.anufriev.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anufriev.data.db.contract.FeedBackContract
import com.anufriev.data.db.contract.FellowContract
import com.anufriev.data.db.contract.OrganizationContract
import com.anufriev.data.db.contract.PhoneCallContract

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${OrganizationContract.tableName} ADD COLUMN `ratingGoodBad` TEXT NOT NULL DEFAULT '0/0'")
    }
}

val MIGRATION_2_3 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${FeedBackContract.tableName} ADD COLUMN `imei` TEXT NOT NULL")
        database.execSQL("ALTER TABLE ${OrganizationContract.tableName} ADD COLUMN `web` TEXT NOT NULL DEFAULT ''")
        database.execSQL("CREATE TABLE IF NOT EXISTS `${FellowContract.tableName}` (`id` INTEGER NOT NULL, `city` TEXT NOT NULL, `description` TEXT NOT NULL, `date` TEXT NOT NULL, `phone` TEXT NOT NULL, PRIMARY KEY(`id`))")
    }
}

val MIGRATE_3_4 = object : Migration(3,4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `${PhoneCallContract.tableName}` (`id` INTEGER NOT NULL, `time` INTEGER NOT NULL, `uid` TEXT NOT NULL, `idOrg` INTEGER NOT NULL, PRIMARY KEY(`id`))")
    }

}
val MIGRATE_4_5 = object : Migration(4,5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `${PhoneCallContract.tableName}`")
        database.execSQL("CREATE TABLE IF NOT EXISTS `${PhoneCallContract.tableName}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `time` INTEGER NOT NULL, `uid` TEXT NOT NULL, `idOrg` INTEGER NOT NULL)\n")
    }

}