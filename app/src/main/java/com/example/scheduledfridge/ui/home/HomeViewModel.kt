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
    val isSelectingMode = MutableLiveData<Boolean>()
    val selectedProducts = MutableLiveData<ArrayList<Product>>()


    init {
        val dao = ProductDatabase.getDatabase(application).productDao()
        repository = ApplicationRepository(dao)
        allProducts = repository.allProducts
        isSelectingMode.value = false
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
    fun setSelectingMode (isSelected: Boolean){
        isSelectingMode.value = isSelected
    }
    fun getSelectingMode(): Boolean{
        return isSelectingMode.value!!
    }
    fun setSelectedProducts(products: ArrayList<Product>){
        selectedProducts.value = products
    }
    fun getSelectedProducts(): ArrayList<Product>{
        return if(selectedProducts.value != null) {
            selectedProducts.value!!
        }else{
            ArrayList()
        }
    }
}

