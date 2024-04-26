package com.example.smarthomeapp.ui.user.temperature

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentTemperatureBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemperatureFragment : Fragment(R.layout.fragment_temperature) {
    private val binding by viewBinding(FragmentTemperatureBinding::bind)
}