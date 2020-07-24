package com.example.scheduledfridge.ui.home

import android.app.Application
import android.widget.ArrayAdapter
import androidx.lifecycle.*
import com.example.scheduledfridge.R
import com.example.scheduledfridge.database.ApplicationRepository
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.database.ProductDatabase
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
     private val repository: ApplicationRepository
    val allProducts: LiveData<List<Product>>




    init {
        val categoryArrayAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            application,
            R.array.types,
            R.layout.support_simple_spinner_dropdown_item
        )
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
    private fun returnCategoryList(adapter: ArrayAdapter<CharSequence>): ArrayList<String> {
        val categories: ArrayList<String> = ArrayList()
        val size = adapter.count
        var i = 0
        while(i < size){
            categories.add(adapter.getItem(i).toString())
            i++
        }
        return categories

    }


}


