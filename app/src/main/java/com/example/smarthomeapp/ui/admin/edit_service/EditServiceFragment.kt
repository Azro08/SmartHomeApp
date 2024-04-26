package com.example.smarthomeapp.ui.admin.edit_service

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentEditServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditServiceFragment : Fragment(R.layout.fragment_edit_service) {
    private val binding by viewBinding(FragmentEditServiceBinding::bind)
}