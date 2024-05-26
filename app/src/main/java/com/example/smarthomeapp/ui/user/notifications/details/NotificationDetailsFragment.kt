package com.example.smarthomeapp.ui.user.notifications.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Notification
import com.example.smarthomeapp.data.model.Priority
import com.example.smarthomeapp.databinding.FragmentNotificationDetailsBinding

class NotificationDetailsFragment : Fragment(R.layout.fragment_notification_details) {
    private val binding by viewBinding(FragmentNotificationDetailsBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val notification: Notification? = arguments?.getParcelable("notification")

        notification?.let {
            binding.notificationDetails.text = it.message

            when (it.priority) {
                Priority.HIGH.name -> binding.container.setBackgroundResource(R.drawable.orange_rounded_edges_background)
                Priority.MEDIUM.name -> binding.container.setBackgroundResource(R.drawable.blue_rounded_edges_background)
                Priority.LOW.name -> binding.container.setBackgroundResource(R.drawable.purple_rounded_edges_background)
                else -> binding.container.setBackgroundResource(R.drawable.blue_rounded_edges_background)
            }

        }

    }
}