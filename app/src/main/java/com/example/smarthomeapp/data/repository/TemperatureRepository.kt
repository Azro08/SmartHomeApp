package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.api.TemperatureApiService
import com.example.smarthomeapp.data.model.TemperatureStatsResponse
import retrofit2.HttpException
import javax.inject.Inject

class TemperatureRepository @Inject constructor(
    private val temperatureApiService: TemperatureApiService
) {

    suspend fun getTemperatureStats(): TemperatureStatsResponse? {
        return try {
            temperatureApiService.getTemperatureStats()
        } catch (e: HttpException) {
            Log.d("TempError", e.message.toString())
            null
        }
    }

    suspend fun updateTemperatureStats(temperature: Int, humidity : Int) : String {
        return try {
            temperatureApiService.updateTemperatureStats(temperature = temperature, humidity = humidity)
            "Done"
        } catch (e: HttpException) {
            Log.d("TempError", e.message.toString())
            e.message.toString()
        }
    }

}