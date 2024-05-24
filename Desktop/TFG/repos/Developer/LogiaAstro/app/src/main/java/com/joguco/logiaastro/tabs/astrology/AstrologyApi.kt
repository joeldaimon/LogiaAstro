package com.joguco.logiaastro.tabs.astrology

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AstrologyApi {
    @Headers(
        "Content-Type: application/json",
        "x-api-key: SL08OU7QDr34yTmL5JTc37aMM9aktIHK3QXw3b2L"
    )
    @POST("planets/")
    fun getPlanets(@Body request: ChartRequest): Call<String>
}