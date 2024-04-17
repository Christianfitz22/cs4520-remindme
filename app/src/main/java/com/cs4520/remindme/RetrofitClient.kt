package com.cs4520.remindme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitClient {
    companion object{
        fun getRetrofitInstance(): APIEndPoint {
            return Retrofit.Builder()
                .baseUrl(API.BASE_URL).addConverterFactory(ScalarsConverterFactory.create())
                .build().create(APIEndPoint::class.java)
        }
    }
}