package com.example.scheduledfridge.database
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import java.time.LocalDate
import java.util.*

class ApplicationRepository(private val productDao:ProductDao, private val statisticsDao: StatisticsDao) {
     val allProducts:LiveData<List<Product>> = productDao.selectAllProducts()
     val allStatistic: LiveData<List<Statistic>> = statisticsDao.selectAllStatistics()
    fun insertProduct(product: Product){
        productDao.insert(product)
    }
    @SuppressLint("NewApi")
    fun deleteProduct(product: Product,eaten:Boolean){
        productDao.delete(product)
        statisticsDao.insert(Statistic(UUID.randomUUID().toString(),product.productName,LocalDate.now().toString(),eaten))
    }
    fun updateProduct(product: Product){
        productDao.update(product)
    }
    fun insertToStatistics(statistic: Statistic){
        statisticsDao.insert(statistic)
    }



}