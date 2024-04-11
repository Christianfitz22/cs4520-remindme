package com.cs4520.remindme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    fun insert(product: ProductDBEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductDBEntry>)

    @Query("DELETE FROM product_list")
    fun deleteAll()

    @Query("SELECT * FROM product_list")
    fun getAll(): List<ProductDBEntry>
}