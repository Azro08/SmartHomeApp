package com.example.smarthomeapp.data.model


import com.google.gson.annotations.SerializedName

data class Feed(
    @SerializedName("field1")
    val field1: String,
    @SerializedName("field2")
    val field2: String,
    @SerializedName("field3")
    val field3: String,
    @SerializedName("field4")
    val field4: String
)