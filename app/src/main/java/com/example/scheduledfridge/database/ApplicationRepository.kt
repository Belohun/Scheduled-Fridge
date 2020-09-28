package com.example.scheduledfridge.database

import androidx.lifecycle.LiveData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ApplicationRepository(private val productDao:ProductDao, private val statisticsDao: StatisticsDao, private val historyDao: HistoryDao) {
     val allProducts:LiveData<List<Product>> = productDao.selectAllProducts()
     val allStatistic: LiveData<List<Statistic>> = statisticsDao.selectAllStatistics()
    val allHistory: LiveData<List<History>> = historyDao.selectAllHistory()

    private val formatter=
        DateTimeFormatter.ofPattern("d/M/yyyy")!!
    fun insertProduct(product: Product){
        productDao.insert(product)
        historyDao.insert(History(UUID.randomUUID().toString(),product.productAddedDate,product.productType,product.productName,"Added"))
    }

    fun deleteProduct(product: Product,eaten:Boolean){
        productDao.delete(product)
        statisticsDao.insert(Statistic(UUID.randomUUID().toString(),product.productName,LocalDate.now().format(formatter).toString(),eaten))
        val action  =
            if(eaten){
                "Eaten"
            }else
            {
                "ThrownAway"
            }

        historyDao.insert(History(UUID.randomUUID().toString(),LocalDate.now().format(formatter).toString(),product.productType,product.productName,action))
    }

    fun updateProduct(product: Product){
        productDao.update(product)
        historyDao.insert(History(UUID.randomUUID().toString(),LocalDate.now().format(formatter).toString(),product.productType,product.productName,"Updated"))
    }



}