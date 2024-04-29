package com.example.smarthomeapp.ui.admin.send_notification

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Notification
import com.example.smarthomeapp.data.model.Priority
import com.example.smarthomeapp.databinding.FragmentSendNotificationBinding
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.DateUtils
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SendNotificationFragment : Fragment(R.layout.fragment_send_notification) {
    private val binding by viewBinding(FragmentSendNotificationBinding::bind)
    private val viewModel: SendNotificationViewModel by viewModels()
    private val prioritiesList = listOf(Priority.HIGH.name, Priority.MEDIUM.name, Priority.LOW.name)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSpinner()
        binding.buttonSendNotification.setOnClickListener {
            if (binding.editTextMessage.text.isNotEmpty() && binding.editTextTitle.text.isNotEmpty()
                && binding.spinnerPriorities.selectedItem.toString().isNotEmpty()
            ) sendNotification()
            else Toast.makeText(context, R.string.fill_upFields, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setSpinner() {
        val spinnerPriorityAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            prioritiesList
        )
        binding.spinnerPriorities.adapter = spinnerPriorityAdapter
    }

    private fun sendNotification() {
        lifecycleScope.launch {
            val title = binding.editTextTitle.text.toString()
            val message = binding.editTextMessage.text.toString()
            val date = DateUtils.getCurrentDateAndTime()
            val id = Constants.generateRandomId()
            val priority = binding.spinnerPriorities.selectedItem.toString()
            val notification = Notification(id, title, message, priority, date)
            viewModel.sendNotification(notification)
            viewModel.sendNotificationState.collect { state ->
                when (state) {
                    is ScreenState.Loading -> binding.buttonSendNotification.visibility = View.GONE
                    is ScreenState.Success -> {
                        binding.buttonSendNotification.visibility = View.VISIBLE
                        binding.editTextTitle.setText("")
                        binding.editTextMessage.setText("")
                        Toast.makeText(
                            context,
                            getString(R.string.notification_sent), Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ScreenState.Error -> {
                        binding.buttonSendNotification.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            state.message ?: getString(R.string.failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}