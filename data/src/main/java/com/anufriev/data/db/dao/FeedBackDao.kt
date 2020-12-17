package com.anufriev.data.db.dao

import androidx.room.*
import com.anufriev.data.db.contract.FeedBackContract
import com.anufriev.data.db.entities.FeedBackDaoEntity

@Dao
interface FeedBackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFeedBackList(feedBackList: List<FeedBackDaoEntity>)

    @Query("SELECT * FROM ${FeedBackContract.tableName} WHERE ${FeedBackContract.Column.idOrg} = :id")
    fun getFeedBackList(id:Int): List<FeedBackDaoEntity>

    @Query("SELECT * FROM ${FeedBackContract.tableName} WHERE ${FeedBackContract.Column.idOrg} = :id and ${FeedBackContract.Column.uid} = :uid ORDER BY ID DESC LIMIT 1")
    fun getLastFeedBack(id:Int, uid:String):FeedBackDaoEntity

    @Update
    fun updateFeedBack(item:FeedBackDaoEntity)
}