package com.developer_harshpatel.demo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.developer_harshpatel.demo.data.model.History

@Dao
interface HistoryDao {

    // Dao includes methods to access database

    //to insert entry in db
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(history: History)

    //This will return list based on specific user id.
    @Query("Select * from historyTable where userId = :userId order by id DESC")
    fun getHistory(userId: Int): LiveData<List<History>>

}