package com.cs4520.remindme

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException

class ProductCoroutineWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        println("Worker work function ran")
        var successResult = false;

        withContext(Dispatchers.IO) {
            val database = DatabaseHolder.database
            val productDao = database.productDao()
            val service = RetrofitClient.makeRetrofitService()

            val response: Response<ArrayList<ProductData>>

            try {
                response = service.getProductList()
            } catch (e: UnknownHostException) {
                // network failed to access when the work was needed
                return@withContext
            }

            val productDataEntries = ArrayList<ProductDBEntry>()

            for (item in response.body()!!) {
                productDataEntries.add(ProductDBEntry(0, item.name, item.type, item.expiryDate, item.price))
            }

            productDao.insertAll(productDataEntries)

            successResult = true
        }

        if (successResult) {
            return Result.success()
        } else {
            return Result.failure()
        }
    }
}