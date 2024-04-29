package com.example.smarthomeapp.data.api

import com.example.smarthomeapp.data.model.TemperatureStatsResponse
import com.example.smarthomeapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface TemperatureApiService {

    //api_key=FE5DN4MB1OUV31YB&results=2

    @GET("channels/2521942/feeds.json")
    suspend fun getTemperatureStats(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("results") results: Int = 1
    ) : TemperatureStatsResponse

    @GET("update")
    suspend fun updateTemperatureStats(
        @Query("api_key") apiKey: String = "3V33RJSTJYNP61DZ",
        @Query("field1") temperature : Int,
        @Query("field2") humidity : Int
    )
}