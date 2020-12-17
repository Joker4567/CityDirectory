package com.anufriev.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anufriev.data.db.contract.OrganizationContract
import com.anufriev.data.db.entities.OrganizationDaoEntity

@Dao
interface OrganizationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setOrgList(orgList: List<OrganizationDaoEntity>)

    @Query("SELECT * FROM ${OrganizationContract.tableName}")
    fun getOrgList(): List<OrganizationDaoEntity>
}