package com.cs4520.remindme

class DatabaseRepository {
    private val retrofit = RetrofitClient.getRetrofitInstance()

    suspend fun getAllRepository() = retrofit.getReminderList()
}