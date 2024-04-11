package com.cs4520.remindme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class ProductViewModel() : ViewModel() {
    private val _productResponseData = MutableLiveData<ArrayList<ProductData>>()
    // rather than ProductResponseModel, this needs to be an ArrayList of ProductData
    val productResponseData : LiveData<ArrayList<ProductData>> = _productResponseData

    //var productAdaptor: ProductAdaptor = ProductAdaptor()

    private lateinit var database: ProductDatabase

    private val _apiState = MutableLiveData<ApiCallState>()
    val apiState : LiveData<ApiCallState> = _apiState



    init {
        println("init productview run")
        // database must be set here
        database = DatabaseHolder.database
        //makeApiCall()
    }

    /*
    fun getAdaptor(): ProductAdaptor {
        return productAdaptor
    }

    fun setAdaptorData(data: ArrayList<ProductData>) {
        productAdaptor.setData(data)
    }
    */

    fun getProductData(): LiveData<ArrayList<ProductData>> {
        return productResponseData
    }

    fun getApiCallState(): LiveData<ApiCallState> {
        return apiState
    }

    fun makeWorkRequest() {
        println("Work request made")
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<ProductCoroutineWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()
        WorkManagerHolder.workManager.enqueueUniquePeriodicWork("productUpdater",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest)
    }

    fun makeApiCall() {
        println("api call function begins")
        _apiState.value = ApiCallState.REQUESTED

        val productDao = database.productDao()

        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable -> throwable.printStackTrace()}

        val service = RetrofitClient.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
            val response: Response<ArrayList<ProductData>>

            try {
                response = service.getProductList()
            } catch (e: UnknownHostException) {
                withContext(Dispatchers.Main) {
                    // if database is full, populate with database instead
                    // else, update with null value
                    println("network failure recognized")
                    val productDataEntries = productDao.getAll()

                    if (productDataEntries.isEmpty()) {
                        println("product data entries empty")
                        _productResponseData.value = ArrayList<ProductData>()
                        _apiState.value = ApiCallState.EMPTY
                    } else {
                        println("product data entries found")
                        val productData = ArrayList<ProductData>()
                        println("data entries size: " + productDataEntries.size)

                        for (item in productDataEntries) {
                            productData.add(ProductData(item.name, item.type, item.expiryDate, item.price))
                        }

                        _productResponseData.value = productData
                        _apiState.value = ApiCallState.SUCCESS
                    }
                }
                return@launch
            }


            println("response gotten, is " + response.isSuccessful)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        println("response is successful")
                        // update database with gotten response
                        if (response.body()!!.isEmpty()) {
                            _productResponseData.value = ArrayList<ProductData>()
                            _apiState.value = ApiCallState.EMPTY
                            return@withContext
                        }

                        _productResponseData.value = response.body()
                        _apiState.value = ApiCallState.SUCCESS

                        val productDataEntries = ArrayList<ProductDBEntry>()

                        println("size of response body: " + response.body()!!.size)

                        for (item in response.body()!!) {
                            productDataEntries.add(ProductDBEntry(0, item.name, item.type, item.expiryDate, item.price))
                        }

                        println("size of dao insertion: " + productDataEntries.size)

                        //productDao.deleteAll()
                        productDao.insertAll(productDataEntries)

                    } else {
                        _productResponseData.value = ArrayList<ProductData>()
                        _apiState.value = ApiCallState.ERROR
                        println("Attempted to get all products but api call failed")
                    }
                } catch (e: HttpException) {
                    println("HTTP exception found")
                    _apiState.value = ApiCallState.FAILURE
                } catch (e: Throwable) {
                    println("Unknown exception found")
                    _apiState.value = ApiCallState.FAILURE
                }
            }
        }
    }
}