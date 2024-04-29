package com.example.smarthomeapp.util

import android.content.Context

class FCMTokenManager(private val context: Context) {

    fun saveToken(token: String) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Constants.FCM_TOKEN_KEY, token).apply()
    }

}