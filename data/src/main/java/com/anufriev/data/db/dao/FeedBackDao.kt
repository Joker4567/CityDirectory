package com.anufriev.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anufriev.data.db.contract.FeedBackContract
import com.anufriev.data.db.entities.FeedBackDaoEntity

@Dao
interface FeedBackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFeedBackList(feedBackList: List<FeedBackDaoEntity>)

    @Query("SELECT * FROM ${FeedBackContract.tableName} WHERE ${FeedBackContract.Column.idOrg} = :id")
    fun getFeedBackList(id:Int): List<FeedBackDaoEntity>
}