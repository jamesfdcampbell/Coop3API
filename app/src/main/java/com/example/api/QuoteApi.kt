package com.example.api

import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApi {
    @GET("/v1/quotes")
    suspend fun getRandomQuote(@Query("category") category: String?): List<Quote>
}