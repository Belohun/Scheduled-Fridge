package com.example.scheduledfridge.ui.home
import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.scheduledfridge.database.ApplicationRepository
import com.example.scheduledfridge.database.Product
import com.example.scheduledfridge.database.ProductDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    fun scheduleNotification(){
        val notificationManager =
            ContextCompat.getSystemService(
                this.getApplication(),
                NotificationManager::class.java
            ) as NotificationManager


    }
    private suspend fun saveTime(triggerTime: Long) =
        withContext(Dispatchers.IO) {
           // prefs.edit().putLong(TRIGGER_TIME, triggerTime).apply()
        }


}


