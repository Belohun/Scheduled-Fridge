package com.example.scheduledfridge.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HISTORY")
data class History (
@PrimaryKey
val id: String,
val date: String,
val type: String,
val name: String,
val action: String
)
