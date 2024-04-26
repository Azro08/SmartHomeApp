package com.example.smarthomeapp.ui.shared.service_details

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentServiceDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceDetailsFragment : Fragment(R.layout.fragment_service_details) {
    private val binding by viewBinding(FragmentServiceDetailsBinding::bind)
}