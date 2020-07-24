package com.example.scheduledfridge.database
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ApplicationRepository(private val dao:ProductDao) {
     val allProducts:LiveData<List<Product>> = dao.selectAllProducts()
    fun insert(product: Product){
        dao.insert(product)
    }
    fun delete(product: Product){
        dao.delete(product)
    }



}