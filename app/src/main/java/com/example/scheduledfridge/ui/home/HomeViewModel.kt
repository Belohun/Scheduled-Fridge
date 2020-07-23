package com.example.scheduledfridge.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledfridge.database.ApplicationRepository
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.database.ProductDatabase
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
     private val repository: ApplicationRepository
    val allProducts: LiveData<List<Product>>
     init {
         val dao = ProductDatabase.getDatabase(application).productDao()
         repository = ApplicationRepository(dao)
         allProducts = repository.allProducts
     }
    fun insert(product: Product){
        viewModelScope.launch {
            repository.insert(product)
        }
    }
    fun delete(product: Product){
        viewModelScope.launch {
            repository.delete(product)
        }
    }

}