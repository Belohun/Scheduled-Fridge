package com.example.scheduledfridge.ui.productDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledfridge.database.Product
import kotlinx.coroutines.launch

class ProductDetailsViewModel(application: Application):AndroidViewModel(application) {
    private var _product = MutableLiveData<Product>()


    fun setCurrentProduct(product: Product){
        viewModelScope.launch {
            _product.value = product
        }
    }

    val currentProduct: LiveData<Product> =_product
}