package com.example.scheduledfridge.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.scheduledfridge.database.ApplicationRepository
import com.example.scheduledfridge.database.History
import com.example.scheduledfridge.database.ProductDatabase

class HistoryViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: ApplicationRepository
    var allHistory: LiveData<List<History>>


    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        val statisticsDao = ProductDatabase.getDatabase(application).statisticsDao()
        val historyDao = ProductDatabase.getDatabase(application).historyDao()
        repository = ApplicationRepository(productDao,statisticsDao,historyDao)
        allHistory = repository.allHistory
    }
}