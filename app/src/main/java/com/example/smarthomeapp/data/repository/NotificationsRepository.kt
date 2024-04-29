package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.model.Notification
import com.example.smarthomeapp.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    firestore: FirebaseFirestore
) {

    private val collection = firestore.collection("notifications")

    // Function to send notification and save it
    suspend fun sendNotificationAndSave(notification: Notification): String {
        return try {
            // Send notification to topic "all_users"
            sendNotification(notification)

            // Save notification in Firestore
            saveNotificationInFirestore(notification)
            "Done"
        } catch (e: Exception) {
            // Handle exceptions
            Log.d("MsgExc", e.message.toString())
            e.message.toString()
        }
    }

    // Function to get all saved notifications
    suspend fun getAllSavedNotifications(): List<Notification> {
        return try {
            collection
                .orderBy("dateAndTime")
                .get()
                .await()
                .toObjects(Notification::class.java)
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
            emptyList()
        }
    }

    // Function to send notification to all users
    private fun sendNotification(notification: Notification) {
        try {
            val data = mapOf(
                "title" to notification.title,
                "body" to notification.message
            )
            val remoteMessage = RemoteMessage.Builder("${Constants.SENDER_ID}@fcm.googleapis.com")
                .setMessageId(notification.id)
                .apply {
                    data.forEach { (key, value) ->
                        addData(key, value)
                    }
                }
                .build()

            firebaseMessaging.send(remoteMessage)
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("MsgExc", it) }
        }
    }

    private suspend fun saveNotificationInFirestore(notification: Notification) {
        try {
            collection
                .document(notification.id)
                .set(notification)
                .await()
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        }
    }
}
