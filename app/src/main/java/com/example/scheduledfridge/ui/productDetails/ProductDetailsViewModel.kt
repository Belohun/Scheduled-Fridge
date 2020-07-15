package com.example.scheduledfridge.ui.productDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledfridge.database.Product
import kotlinx.coroutines.launch

class ProductDetailsViewModel(application: Application):AndroidViewModel(application) {
  val currentProduct = MutableLiveData<Product>()
    fun setCurrentProduct(product: Product){
        currentProduct.value = product

    }


}