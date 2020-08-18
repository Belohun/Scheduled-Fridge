package com.example.scheduledfridge.ui.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.scheduledfridge.database.ApplicationRepository
import com.example.scheduledfridge.database.ProductDatabase
import com.example.scheduledfridge.database.Statistic

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ApplicationRepository
    var allStatistic: LiveData<List<Statistic>>


    init {
        val productDao = ProductDatabase.getDatabase(application).productDao()
        val statisticsDao = ProductDatabase.getDatabase(application).statisticsDao()
        repository = ApplicationRepository(productDao,statisticsDao)
        allStatistic = repository.allStatistic
    }


}