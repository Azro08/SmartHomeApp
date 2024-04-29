package com.example.smarthomeapp.ui.shared.order_history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Order
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.databinding.FragmentOrderHistoryBinding
import com.example.smarthomeapp.ui.shared.auth.AuthActivity
import com.example.smarthomeapp.util.AuthManager
import com.example.smarthomeapp.util.ScreenState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderHistoryFragment : Fragment(R.layout.fragment_order_history) {
    private val binding by viewBinding(FragmentOrderHistoryBinding::bind)
    private val viewModel: OrderHistoryViewModel by viewModels()

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private var adapter: RvOrderAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (authManager.getRole() == UserRole.MASTER_ROLE.name) {
            binding.btnLogout.visibility = View.VISIBLE
            binding.btnLogout.setOnClickListener {
                authManager.removeRole()
                authManager.removeUser()
                firebaseAuth.signOut()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }
            getMastersOrders()
        } else if (authManager.getRole() == UserRole.ADMIN_ROLE.name) getAllOrders()
    }

    private fun getAllOrders() {
        lifecycleScope.launch {
            viewModel.allOrders.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> {
                        displayHistory(state.data!!)
                    }
                }
            }
        }
    }

    private fun getMastersOrders() {
        lifecycleScope.launch {
            viewModel.getMasterOrders()
            viewModel.usersOrder.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> {
                        displayHistory(state.data!!)
                    }
                }
            }
        }
    }

    private fun displayHistory(orderList: List<Order>) {
        adapter = RvOrderAdapter(orderList)
        binding.rvOrderHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderHistory.setHasFixedSize(true)
        binding.rvOrderHistory.adapter = adapter
    }
}