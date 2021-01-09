package com.anufriev.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anufriev.data.db.contract.FellowContract
import java.io.Serializable

@Entity(tableName = FellowContract.tableName)
data class FellowDaoEntity(
    @PrimaryKey
    @ColumnInfo(name = FellowContract.Column.id)
    val id:Int,
    @ColumnInfo(name = FellowContract.Column.city)
    val city:String,
    @ColumnInfo(name = FellowContract.Column.description)
    val description:String,
    @ColumnInfo(name = FellowContract.Column.date)
    val date:String,
    @ColumnInfo(name = FellowContract.Column.phone)
    val phone:String
):Serializable
