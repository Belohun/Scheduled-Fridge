package com.example.scheduledfridge.ui.home
import android.app.Application
import androidx.lifecycle.*
import com.example.scheduledfridge.database.*
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ApplicationRepository
    var allProducts: LiveData<List<Product>>
    private var allStatistic: LiveData<List<Statistic>>
    val isSelectingMode = MutableLiveData<Boolean>()
    private val selectedProducts = MutableLiveData<ArrayList<Product>>()


    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        val statisticsDao = ProductDatabase.getDatabase(application).statisticsDao()
        val historyDao = ProductDatabase.getDatabase(application).historyDao()
        repository = ApplicationRepository(productDao,statisticsDao,historyDao)
        allProducts = repository.allProducts
        allStatistic = repository.allStatistic
        isSelectingMode.value = false
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }
    fun updateProduct(product: Product){
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product,eaten:Boolean) {
        viewModelScope.launch {
            repository.deleteProduct(product,eaten)
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

