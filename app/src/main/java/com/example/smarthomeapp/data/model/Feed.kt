package com.example.smarthomeapp.data.model


import com.google.gson.annotations.SerializedName

data class Feed(
    @SerializedName("field1")
    val field1: String?,
    @SerializedName("field2")
    val field2: String?,
)