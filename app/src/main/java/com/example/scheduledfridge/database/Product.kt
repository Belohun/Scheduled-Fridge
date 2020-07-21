package com.example.scheduledfridge.database

import androidx.room.Entity
import androidx.room.PrimaryKey

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