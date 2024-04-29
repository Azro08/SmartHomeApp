package com.example.smarthomeapp.ui.user.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Notification
import com.example.smarthomeapp.data.model.Priority
import com.example.smarthomeapp.databinding.NotificationViewHolderBinding

class NotificationAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: NotificationViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.textViewNotificationTitle.text = notification.title
            binding.textViewNotificationMessage.text = notification.message
            binding.orderTime.text = notification.dateAndTime

            // Apply background color based on priority
            when (notification.priority) {
                Priority.HIGH.name -> binding.llNotification.setBackgroundResource(R.drawable.orange_rounded_edges_background)
                Priority.MEDIUM.name -> binding.llNotification.setBackgroundResource(R.drawable.blue_rounded_edges_background)
                Priority.LOW.name -> binding.llNotification.setBackgroundResource(R.drawable.light_blue_rounded_edges_background)
                else -> binding.llNotification.setBackgroundResource(R.drawable.blue_rounded_edges_background)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NotificationViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}
