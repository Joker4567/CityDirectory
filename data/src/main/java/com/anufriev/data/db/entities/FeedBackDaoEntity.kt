package com.anufriev.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anufriev.data.db.contract.FeedBackContract
import java.io.Serializable

@Entity(tableName = FeedBackContract.tableName)
data class FeedBackDaoEntity(
    @PrimaryKey
    @ColumnInfo(name = FeedBackContract.Column.id)
    val id:Int,
    @ColumnInfo(name = FeedBackContract.Column.idOrg)
    val idOrg:Int,
    @ColumnInfo(name = FeedBackContract.Column.state)
    val state:Boolean,
    @ColumnInfo(name = FeedBackContract.Column.description)
    val description:String,
    @ColumnInfo(name = FeedBackContract.Column.time)
    val time:Long,
    @ColumnInfo(name = FeedBackContract.Column.uid)
    val uid:String
):Serializable
