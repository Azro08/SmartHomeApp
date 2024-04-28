package com.example.smarthomeapp.data.model


import com.google.gson.annotations.SerializedName

data class TemperatureStatsResponse(
    @SerializedName("feeds")
    val feeds: List<Feed>
)