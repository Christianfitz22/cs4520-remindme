package com.cs4520.remindme

import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {
    @GET(Api.ENDPOINT)
    suspend fun getProductList(): Response<ArrayList<ProductData>>
}