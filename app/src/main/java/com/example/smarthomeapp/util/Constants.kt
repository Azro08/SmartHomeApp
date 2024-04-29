package com.example.smarthomeapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object Constants {
    const val ORDER_ID = "order_id"
    const val SHARED_PREF_NAME = "user_pref"
    const val USER_KEY = "user_key"
    const val ROLE_KEY = "role_key"
    const val SERVICE_KEY = "service_key"
    const val BASE_URL = "https://api.thingspeak.com/"
    const val API_KEY = "FE5DN4MB1OUV31YB"

    const val CHANNEL_ID = "notification_channel"
    const val CHANNEL_NAME = "com.example.smarthomeapp"

    fun generateRandomId(): String {
        val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random =
            Random(System.currentTimeMillis()) // Seed the random number generator with the current time

        val randomString = StringBuilder(28)

        for (i in 0 until 28) {
            val randomIndex = random.nextInt(characters.length)
            randomString.append(characters[randomIndex])
        }

        return randomString.toString()
    }

}