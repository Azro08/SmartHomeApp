package com.example.smarthomeapp.ui.admin.add_service

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Service
import com.example.smarthomeapp.databinding.FragmentAddServiceBinding
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddServiceFragment : Fragment(R.layout.fragment_add_service) {
    private val binding by viewBinding(FragmentAddServiceBinding::bind)
    private val viewModel: AddServiceViewModel by viewModels()
    private var serviceId = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        serviceId = arguments?.getString(Constants.SERVICE_KEY) ?: ""
        if (serviceId != "") {
            getServiceDetails()
            binding.btnDeleteService.visibility = View.VISIBLE
        }
        binding.btnAddService.setOnClickListener {
            if (allFieldsFilled()) {
                saveService()
            }
        }

    }

    private fun saveService() {
        lifecycleScope.launch {
            val id = if (serviceId == "") Constants.generateRandomId() else serviceId
            val title = binding.editTextServiceTitle.text.toString()
            val price = binding.editTextServicePrice.text.toString().toDouble()
            val description = binding.editTextServiceDescription.text.toString()
            val recommendedMasters = binding.editTextServiceRecommendedMasters.text.toString()
            val service = Service(
                id = id,
                title = title,
                price = price,
                description = description,
                recommendedMasters = recommendedMasters
            )
            viewModel.saveService(service)
        }
    }

    private fun allFieldsFilled(): Boolean = with(binding) {
        return (editTextServiceTitle.text!!.isNotBlank() && editTextServicePrice.text!!.isNotBlank() &&
                editTextServiceDescription.text!!.isNotBlank()) && editTextServiceRecommendedMasters.text!!.isNotBlank()
    }

    private fun getServiceDetails() {
        lifecycleScope.launch {
            viewModel.getServiceState.collect { state ->
                when (state) {

                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        context,
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> {
                        binding.editTextServiceTitle.setText(state.data!!.title)
                        binding.editTextServiceDescription.setText(state.data.description)
                        val price = state.data.price.toString() + " BYN"
                        binding.editTextServicePrice.setText(price)
                        binding.editTextServiceRecommendedMasters.setText(state.data.recommendedMasters)
                        binding.btnDeleteService.setOnClickListener {
                            viewModel.deleteService(state.data)
                            binding.btnDeleteService.visibility = View.GONE
                            binding.btnAddService.visibility = View.GONE
                            navBack()
                        }
                    }

                }
            }
        }
    }

    private fun navBack() {
        lifecycleScope.launch {
            delay(1000)
            findNavController().popBackStack()
        }
    }

}
