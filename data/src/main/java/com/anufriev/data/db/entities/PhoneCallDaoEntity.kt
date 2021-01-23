package com.anufriev.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anufriev.data.db.contract.PhoneCallContract

@Entity(tableName = PhoneCallContract.tableName)
data class PhoneCallDaoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PhoneCallContract.Column.id)
    val id:Int,
    @ColumnInfo(name = PhoneCallContract.Column.time)
    val time:Long,
    @ColumnInfo(name = PhoneCallContract.Column.uid)
    val uid:String,
    @ColumnInfo(name = PhoneCallContract.Column.idOrg)
    val idOrg:Int
)
