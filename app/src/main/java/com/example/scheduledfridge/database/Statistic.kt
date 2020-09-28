package com.example.scheduledfridge.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "STATISTICS")
data class Statistic (
    @PrimaryKey
    val id: String,
    val productName: String,
    val deleted_date : String,
    val eaten: Boolean
)