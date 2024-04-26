package com.example.smarthomeapp.ui.user.order_service

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentOrderServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderServiceFragment : Fragment(R.layout.fragment_order_service) {
    private val binding by viewBinding(FragmentOrderServiceBinding::bind)
}