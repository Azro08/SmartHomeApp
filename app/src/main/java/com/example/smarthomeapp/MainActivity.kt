package com.example.smarthomeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.databinding.ActivityMainBinding
import com.example.smarthomeapp.ui.admin.AdminActivity
import com.example.smarthomeapp.ui.shared.auth.AuthActivity
import com.example.smarthomeapp.util.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authManager: AuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!authManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            this.finish()
        } else getRole()
    }

    private fun getRole() {
        val role = authManager.getRole()
        if (role.isEmpty()) {
            startActivity(Intent(this, AuthActivity::class.java))
        } else {
            when (role) {
                UserRole.USER_ROLE.name -> {
                    setBottomNavBar()
                }

                UserRole.ADMIN_ROLE.name -> {
                    startActivity(Intent(this, AdminActivity::class.java))
                    this.finish()
                }

                UserRole.MASTER_ROLE.name -> {
                    enterAsMaster()
                }
            }
        }
    }

    private fun enterAsMaster() {

    }

    private fun setBottomNavBar() {
        val navController = findNavController(R.id.nav_host)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notificationsFragment,
                R.id.temperatureFragment,
                R.id.servicesFragment,
                R.id.profileFragment
            )
        )

        val topLevelDestinations = setOf(
            R.id.notificationsFragment,
            R.id.temperatureFragment,
            R.id.servicesFragment,
            R.id.profileFragment
        )
        // Show the bottom navigation view for top-level destinations only
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in topLevelDestinations) {
                binding.navView.visibility = View.VISIBLE
            } else {
                binding.navView.visibility = View.GONE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}