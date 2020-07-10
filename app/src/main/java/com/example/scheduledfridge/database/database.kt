package com.example.scheduledfridge.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Entity(tableName = "PRODUCTS")
data class Product(
    @PrimaryKey
    var id: Int,
    var productName: String,
    var productType: String,
    var productExpirationDate: String,
    var productAdedDate: String,
    var quantity: Int
    )
@Dao
interface ProductDao{
    @Insert
    fun insert(product: Product)
    @Update
    fun update(product: Product)
    @Delete
    fun delete(product: Product)
    @Query("SELECT * FROM PRODUCTS")
    fun selectAllCitys():LiveData<List<Product>>
    @Query("SELECT id FROM PRODUCTS")
    fun selectAllIds():List<Int>

}
@Database(entities = arrayOf(Product::class),version = 1)
abstract class ProductDatabase: RoomDatabase(){
            abstract fun productDao(): ProductDao
    companion object{
        @Volatile
        private var INSTANCE: ProductDatabase?=null
        fun getDatabase(context: Context): ProductDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java,"PRODUCT_DATABASE").allowMainThreadQueries().build()
                INSTANCE= instance
                return instance
            }
        }
    }
}