package com.example.smarthomeapp.ui.shared.services

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Service
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.databinding.FragmentServicesBinding
import com.example.smarthomeapp.util.AuthManager
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ServicesFragment : Fragment(R.layout.fragment_services) {
    private val binding by viewBinding(FragmentServicesBinding::bind)
    private val viewModel: ServicesViewModel by viewModels()

    @Inject
    lateinit var authManager: AuthManager
    private var rvServicesAdapter: RvServicesAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (authManager.getRole() == UserRole.ADMIN_ROLE.name) setMenu()
        getServices()
        binding.swipeToRefreshLayout.setOnRefreshListener {
            viewModel.refreshServices()
            getServices()
            binding.swipeToRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        viewModel.refreshServices()
        getServices()
        super.onResume()
    }

    private fun getServices() {
        lifecycleScope.launch {
            viewModel.services.collect { state ->
                when (state) {

                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> displayServices(state.data!!)

                }
            }
        }
    }

    private fun displayServices(serviceList: List<Service>) {
        rvServicesAdapter = RvServicesAdapter(serviceList) {
            val bundle = bundleOf(Pair(Constants.SERVICE_KEY, it.id))
            if (authManager.getRole() == UserRole.ADMIN_ROLE.name) {
                findNavController().navigate(R.id.nav_services_add_service, bundle)
            } else findNavController().navigate(R.id.nav_services_details, bundle)
        }
        binding.rvServices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvServices.setHasFixedSize(true)
        binding.rvServices.adapter = rvServicesAdapter
    }

    private fun setMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.itemAddNewElement -> {
                        findNavController().navigate(R.id.nav_services_add_service)
                    }
                }
                return true
                // Handle option Menu Here
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}