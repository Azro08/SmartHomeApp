package com.example.smarthomeapp.ui.shared.order_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentOrderDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : Fragment(R.layout.fragment_order_details) {
    private val binding by viewBinding(FragmentOrderDetailsBinding::bind)
}