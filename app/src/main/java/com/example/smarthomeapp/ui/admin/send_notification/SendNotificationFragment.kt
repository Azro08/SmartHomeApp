package com.example.smarthomeapp.ui.admin.send_notification

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentSendNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendNotificationFragment : Fragment(R.layout.fragment_send_notification) {
    private val binding by viewBinding(FragmentSendNotificationBinding::bind)
}