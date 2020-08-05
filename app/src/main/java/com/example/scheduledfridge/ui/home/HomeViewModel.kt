package com.example.scheduledfridge.ui.home
import android.app.Application
import androidx.lifecycle.*
import com.example.scheduledfridge.database.ApplicationRepository
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.database.ProductDatabase
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ApplicationRepository
    var allProducts: LiveData<List<Product>>
    val isSelectedMode = MutableLiveData<Boolean>()
    val selectedProducts = MutableLiveData<List<Product>>()


    init {
        val dao = ProductDatabase.getDatabase(application).productDao()
        repository = ApplicationRepository(dao)
        allProducts = repository.allProducts
        isSelectedMode.value = false
    }

    fun insert(product: Product) {
        viewModelScope.launch {
            repository.insert(product)
        }
    }

    fun delete(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
        }

    }
    fun setSelectedMode (isSelected: Boolean){
        isSelectedMode.value = isSelected
    }
    fun setSelectedProducts(products: List<Product>){
        selectedProducts.value = products
    }
}

