package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.model.Notification
import com.example.smarthomeapp.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val firestore: FirebaseFirestore
) {

    private val collection = firestore.collection("notifications")

    // Function to send notification and save it
    suspend fun sendNotificationAndSave(notification: Notification) : String{
        return try {
            // Send notification to topic "all_users"
            sendNotificationToTopic(notification)

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
    private fun sendNotificationToTopic(notification: Notification) {
        try {
            // Build notification message
            val message = RemoteMessage.Builder("all_users")
                .setMessageId(Constants.generateRandomId())
                .addData("title", notification.title)
                .addData("message", notification.message)
                .build()

            // Send message to topic "all_users"
            firebaseMessaging.send(message)
        } catch (e: Exception) {
            Log.d("MsgExc", e.message.toString())
        }
    }

    private suspend fun saveNotificationInFirestore(notification: Notification) {
        try {
            // Generate current date and time
            val currentDateAndTime =
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

            // Add date and time to the notification
            val notificationWithDateTime = notification.copy(dateAndTime = currentDateAndTime)

            // Save notification in Firestore
            firestore.collection("notifications")
                .add(notificationWithDateTime)
                .await()
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        }
    }
}
