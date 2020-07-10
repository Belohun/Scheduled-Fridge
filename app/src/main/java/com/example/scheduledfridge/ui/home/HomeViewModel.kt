package com.example.scheduledfridge.ui.home

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
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
    fun getLowestPossibleId(product: List<Product>): Int {
        var i = 0
        while(product.size > i){
            i++
            if(i != product[i].id)
            {
                return i
            }
        }
        return i


    }

    private val _productName = MutableLiveData<String>().apply {
        value = "Apple"
    }
    private val _productGroup = MutableLiveData<String>().apply {
        value = "Fruits"
    }
    private val _productValidity= MutableLiveData<String>().apply {
        value = "7 days left"
    }
    val productName: LiveData<String> = _productName
    val productGroup: LiveData<String> = _productGroup
    val productValidity: LiveData<String> = _productValidity

}