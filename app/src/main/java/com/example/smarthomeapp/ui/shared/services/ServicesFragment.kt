package com.example.smarthomeapp.ui.shared.services

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentServicesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : Fragment(R.layout.fragment_services) {
    private val binding by viewBinding(FragmentServicesBinding::bind)
}