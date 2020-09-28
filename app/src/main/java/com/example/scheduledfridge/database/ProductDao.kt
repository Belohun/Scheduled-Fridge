package com.example.scheduledfridge.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao{
    @Insert
    fun insert(product: Product)
    @Update
    fun update(product: Product)
    @Delete
    fun delete(product: Product)
    @Query("SELECT * FROM PRODUCTS")
    fun selectAllProducts(): LiveData<List<Product>>
    @Query("SELECT id FROM PRODUCTS")
    fun selectAllIds():List<Int>

}