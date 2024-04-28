package com.example.smarthomeapp.data.model

data class Order (
    val id : String = "",
    val serviceId : String = "",
    val serviceTitle : String = "",
    val buyer : String = "",
    val phoneNum : String = "",
    val orderTime : String = "",
    val pickedMaster : String = "",
    val clientComment : String = "",
    val masterComment : String = "",
    )