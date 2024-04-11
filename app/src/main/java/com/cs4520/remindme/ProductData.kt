package com.cs4520.remindme

import com.google.gson.annotations.SerializedName

data class ProductData(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("expiryDate")
    val expiryDate: String?,
    @SerializedName("price")
    val price: Double)