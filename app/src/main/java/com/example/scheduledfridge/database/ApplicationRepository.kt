package com.example.scheduledfridge.database
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ApplicationRepository(private val productDao:ProductDao, private val statisticsDao: StatisticsDao, private val historyDao: HistoryDao) {
     val allProducts:LiveData<List<Product>> = productDao.selectAllProducts()
     val allStatistic: LiveData<List<Statistic>> = statisticsDao.selectAllStatistics()
    val allHistory: LiveData<List<History>> = historyDao.selectAllHistory()
    @SuppressLint("NewApi")
    val formatter=
        DateTimeFormatter.ofPattern("d/M/yyyy")!!
    fun insertProduct(product: Product){
        productDao.insert(product)
        historyDao.insert(History(UUID.randomUUID().toString(),product.productAddedDate,product.productType,product.productName,"Added"))
    }
    @SuppressLint("NewApi")
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
    @SuppressLint("NewApi")
    fun updateProduct(product: Product){
        productDao.update(product)
        historyDao.insert(History(UUID.randomUUID().toString(),LocalDate.now().format(formatter).toString(),product.productType,product.productName,"Updated"))
    }



}