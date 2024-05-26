package com.example.smarthomeapp.ui.user.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Notification
import com.example.smarthomeapp.data.model.Priority
import com.example.smarthomeapp.databinding.NotificationViewHolderBinding

class NotificationAdapter(
    private val notifications: List<Notification>,
    private val listener: (notification: Notification) -> Unit
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: NotificationViewHolderBinding,
        listener: (notification: Notification) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var notification : Notification? = null
        fun bind(currentNotification: Notification) {
            binding.textViewNotificationTitle.text = currentNotification.title
            binding.textViewNotificationMessage.text = currentNotification.message
            binding.orderTime.text = currentNotification.dateAndTime

            // Apply background color based on priority
            when (currentNotification.priority) {
                Priority.HIGH.name -> binding.llNotification.setBackgroundResource(R.drawable.orange_rounded_edges_background)
                Priority.MEDIUM.name -> binding.llNotification.setBackgroundResource(R.drawable.blue_rounded_edges_background)
                Priority.LOW.name -> binding.llNotification.setBackgroundResource(R.drawable.purple_rounded_edges_background)
                else -> binding.llNotification.setBackgroundResource(R.drawable.blue_rounded_edges_background)
            }
            notification = currentNotification
        }

        init {
            binding.root.setOnClickListener {
                listener(notification!!)
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
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}
