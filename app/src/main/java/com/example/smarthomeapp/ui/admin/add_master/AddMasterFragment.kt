package com.example.smarthomeapp.ui.admin.add_master

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentAddMasterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMasterFragment : Fragment(R.layout.fragment_add_master) {
    private val binding by viewBinding(FragmentAddMasterBinding::bind)
}