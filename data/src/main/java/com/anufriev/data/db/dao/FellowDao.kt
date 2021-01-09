package com.anufriev.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anufriev.data.db.contract.FellowContract
import com.anufriev.data.db.entities.FellowDaoEntity

@Dao
interface FellowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFellowList(fellowList: List<FellowDaoEntity>)

    @Query("SELECT * FROM ${FellowContract.tableName} WHERE ${FellowContract.Column.city} = :city")
    fun getFellowList(city:String): List<FellowDaoEntity>

    @Query("DELETE FROM ${FellowContract.tableName}")
    fun deleteAllFellows()
}