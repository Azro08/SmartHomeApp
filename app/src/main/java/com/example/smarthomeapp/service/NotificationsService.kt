package com.example.smarthomeapp.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.smarthomeapp.MainActivity
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.NotificationItemViewBinding
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.FCMTokenManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsService : FirebaseMessagingService() {
    private var _binding: NotificationItemViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationLayout: RemoteViews
    @Inject lateinit var fcmTokenManager: FCMTokenManager
    override fun onCreate() {
        super.onCreate()
        notificationLayout =
            RemoteViews(packageName, R.layout.notification_item_view) // Replace with your layout

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMTOKEN",token)
        fcmTokenManager.saveToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            val title = message.notification!!.title ?: ""
            val messageBody = message.notification!!.body ?: ""
            generateNotification(title, messageBody)
        }
    }

    private fun generateNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        binding.textViewNotificationTitle.text = title
        binding.textViewNotificationMessage.text = messageBody

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setCustomContentView(notificationLayout) // Set custom content view

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())

    }

}