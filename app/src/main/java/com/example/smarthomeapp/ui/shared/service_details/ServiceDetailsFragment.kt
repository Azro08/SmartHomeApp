package com.example.smarthomeapp.ui.shared.service_details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Service
import com.example.smarthomeapp.databinding.FragmentServiceDetailsBinding
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ServiceDetailsFragment : Fragment(R.layout.fragment_service_details) {
    private val binding by viewBinding(FragmentServiceDetailsBinding::bind)
    private val viewModel: ServiceDetailsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getServiceDetails()
    }

    private fun getServiceDetails() {
        lifecycleScope.launch {
            viewModel.service.collect { state ->
                when (state) {

                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> displayServiceDetails(state.data!!)
                }

            }
        }
    }

    private fun displayServiceDetails(service: Service) {
        binding.tvDetailsTitle.text = service.title
        binding.tvDetailsDes.text = service.description
        val price = service.price.toString() + " BYN"
        binding.tvDetailsPrice.text = price
        binding.tvDetailsRecMaster.text = service.recommendedMasters
        binding.btnOrder.setOnClickListener {
            val bundle = bundleOf(Pair(Constants.SERVICE_KEY, service.id))
            findNavController().navigate(R.id.nav_details_order_servise, bundle)
        }
    }
}