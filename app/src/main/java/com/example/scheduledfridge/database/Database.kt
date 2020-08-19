package com.example.scheduledfridge.database
import android.content.Context
import androidx.room.*
import com.example.scheduledfridge.R


@Database(entities = [Product::class,Statistic::class,History::class],version = 1)
abstract class ProductDatabase: RoomDatabase(){
            abstract fun productDao(): ProductDao
            abstract fun statisticsDao():StatisticsDao
            abstract fun historyDao(): HistoryDao
    companion object{
        @Volatile
        private var INSTANCE: ProductDatabase?=null
        fun getDatabase(context: Context): ProductDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java,context.getString(R.string.dataBase_name)).allowMainThreadQueries().build()
                INSTANCE= instance
                return instance
            }
        }
    }
}