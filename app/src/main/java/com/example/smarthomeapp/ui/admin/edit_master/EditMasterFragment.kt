package com.example.smarthomeapp.ui.admin.edit_master

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentEditMasterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMasterFragment : Fragment(R.layout.fragment_edit_master) {
    private val binding by viewBinding(FragmentEditMasterBinding::bind)
}