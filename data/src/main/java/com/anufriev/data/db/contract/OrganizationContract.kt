package com.anufriev.data.db.contract

object OrganizationContract {
    const val tableName = "orgList"
    object Column {
        const val id = "id"
        const val description = "description"
        const val rating = "rating"
        const val name = "name"
        const val phoneNumber = "phoneNumber"
        const val ratingGoodBad = "ratingGoodBad"
    }
}