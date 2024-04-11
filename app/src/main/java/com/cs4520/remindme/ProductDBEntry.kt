package com.cs4520.remindme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_list")
data class ProductDBEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val type: String,
    val expiryDate: String?,
    val price: Double
)