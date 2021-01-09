package com.anufriev.data.db.contract

object FeedBackContract {
    const val tableName = "feedBackList"
    object Column {
        const val id = "id"
        const val idOrg = "idOrg"
        const val state = "state"
        const val description = "description"
        const val time = "time"
        const val uid = "uid"
        const val date = "date"
        const val imei = "imei"
    }
}