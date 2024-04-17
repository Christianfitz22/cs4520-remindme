package com.cs4520.remindme

import retrofit2.Response
import retrofit2.http.GET

interface APIEndPoint {
    @GET(API.ENDPOINT)
    suspend fun getIsDayOff() : Response<String>
}