package com.anufriev.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.anufriev.data.db.contract.OrganizationContract
import com.anufriev.data.entity.Organization
import java.io.Serializable

@Entity(tableName = OrganizationContract.tableName)
data class OrganizationDaoEntity(
    @PrimaryKey
    @ColumnInfo(name = OrganizationContract.Column.id)
    val id:Int,
    @ColumnInfo(name = OrganizationContract.Column.name)
    val name:String,
    @ColumnInfo(name = OrganizationContract.Column.phoneNumber)
    val phoneNumber:String,
    @ColumnInfo(name = OrganizationContract.Column.rating)
    val rating:Int,
    @ColumnInfo(name = OrganizationContract.Column.description)
    val description:String,
    @ColumnInfo(name = OrganizationContract.Column.ratingGoodBad, defaultValue = "0/0")
    var ratingGoodBad:String = "0/0"
):Serializable {
    fun from() = Organization(
        id, name, phoneNumber, rating, description, ratingGoodBad
    )
}
