package com.anufriev.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anufriev.data.db.contract.PhoneCallContract
import com.anufriev.data.db.entities.PhoneCallDaoEntity

@Dao
interface PhoneCallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setPhoneCallList(fellowList: List<PhoneCallDaoEntity>)

    @Query("SELECT * FROM ${PhoneCallContract.tableName} WHERE ${PhoneCallContract.Column.uid} = :uid AND ${PhoneCallContract.Column.idOrg} = :idOrg ORDER BY ID DESC LIMIT 1")
    fun getPhoneCallList(uid:String, idOrg:Int): PhoneCallDaoEntity
}