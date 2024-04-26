package com.example.smarthomeapp.ui.admin.add_service

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentAddServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddServiceFragment : Fragment(R.layout.fragment_add_service) {
    private val binding by viewBinding(FragmentAddServiceBinding::bind)
}