package com.example.scheduledfridge.database

import androidx.lifecycle.LiveData

class ApplicationRepository(private val dao:ProductDao) {
     val allProducts: LiveData<List<Product>> = dao.selectAllCitys()
    suspend fun insert(product: Product){
        dao.insert(product)
    }
    suspend fun delete(product: Product){
        dao.delete(product)
    }



}