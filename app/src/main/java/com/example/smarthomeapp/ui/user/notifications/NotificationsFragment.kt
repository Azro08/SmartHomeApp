package com.example.smarthomeapp.ui.user.notifications

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentNotificationsBinding
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    private val binding by viewBinding(FragmentNotificationsBinding::bind)
    private val viewModel: NotificationsViewModel by viewModels()
    private var adapter: NotificationAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getNotifications()
        binding.swipeToRefreshLayout.setOnRefreshListener {
            getNotifications()
            binding.swipeToRefreshLayout.isRefreshing = false
        }
    }

    private fun getNotifications() {
        lifecycleScope.launch {
            viewModel.getNotifications()
            viewModel.notifications.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {}
                    is ScreenState.Success -> {
                        adapter = NotificationAdapter(state.data!!){
                            val bundle = Bundle().apply {
                                putParcelable("notification", it)
                            }

                            findNavController().navigate(R.id.nav_notififcation_details, bundle)

                        }
                        binding.rvNotifications.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.rvNotifications.setHasFixedSize(true)
                        binding.rvNotifications.adapter = adapter
                    }
                }
            }
        }
    }
}