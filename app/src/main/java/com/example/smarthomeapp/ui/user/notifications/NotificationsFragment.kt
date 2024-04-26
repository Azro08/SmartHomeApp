package com.example.smarthomeapp.ui.user.notifications

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    private val binding by viewBinding(FragmentNotificationsBinding::bind)
}