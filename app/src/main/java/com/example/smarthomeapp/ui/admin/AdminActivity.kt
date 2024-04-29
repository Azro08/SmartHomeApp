package com.example.smarthomeapp.ui.admin

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.ActivityAdminBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {
    private var _binding: ActivityAdminBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminBinding.inflate(layoutInflater)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                getResources().getColor(
                    R.color.light_blue,
                    theme
                )
            )
        )
        setContentView(binding.root)
        setBottomNavBar()
    }

    private fun setBottomNavBar() {
        val navController = findNavController(R.id.adminNavHost)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.servicesFragment2,
                R.id.editMasterFragment,
                R.id.orderHistoryFragment,
                R.id.profileFragment2
            )
        )

        val topLevelDestinations = setOf(
            R.id.servicesFragment2,
            R.id.editMasterFragment,
            R.id.orderHistoryFragment,
            R.id.profileFragment2
        )
        // Show the bottom navigation view for top-level destinations only
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in topLevelDestinations) {
                binding.adminNavView.visibility = View.VISIBLE
            } else {
                binding.adminNavView.visibility = View.GONE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.adminNavView.setupWithNavController(navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}