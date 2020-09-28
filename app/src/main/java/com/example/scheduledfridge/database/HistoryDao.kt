package com.example.scheduledfridge.database
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface HistoryDao {
    @Insert
    fun insert(history: History)
    @Delete
    fun delete (history: History)
    @Query("SELECT * FROM HISTORY")
    fun selectAllHistory(): LiveData<List<History>>

}