package com.dicoding.asclepius.response

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/everything")
    fun getHealthArticles(
        @Query("q") query: String = "cancer",
        @Query("apiKey") apiKey: String = "dee619956d7342dd8a4ad7cb7dcb272e",
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = 10
    ): Call<ResponseApi>

}