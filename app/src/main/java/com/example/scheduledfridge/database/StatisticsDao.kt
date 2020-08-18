package com.example.scheduledfridge.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface StatisticsDao {
    @Insert
    fun insert(statistic: Statistic)
    @Delete
    fun delete (statistic: Statistic)
    @Query("SELECT * FROM STATISTICS")
    fun selectAllStatistics(): LiveData<List<Statistic>>


}