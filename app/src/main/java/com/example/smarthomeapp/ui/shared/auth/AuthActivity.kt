package com.example.smarthomeapp.ui.shared.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smarthomeapp.MainActivity
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.databinding.ActivityAuthBinding
import com.example.smarthomeapp.util.AuthManager
import com.example.smarthomeapp.util.ScreenState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var authManager: AuthManager
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            if (binding.editTextLoginEmail.text.isNotEmpty() && binding.editTextLoginPassword.text.isNotEmpty()) login()
            else Toast.makeText(this, getString(R.string.fillup_fields), Toast.LENGTH_SHORT).show()
        }

    }

    private fun login() = with(binding) {
        val email = editTextLoginEmail.text.toString()
        val password = editTextLoginPassword.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authManager.saveUer(email)
                getUserRole()
            } else {
                Toast.makeText(
                    this@AuthActivity, task.exception?.message, Toast.LENGTH_LONG
                ).show()
            }

        }

    }

    private fun getUserRole() {
        lifecycleScope.launch {
            viewModel.userRole.collect {
                when (it) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Success -> {
                        if (it.data != null) {
                            saveRole(it.data)
                        } else Toast.makeText(
                            this@AuthActivity,
                            "Can't get user role",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is ScreenState.Error -> {
                        Toast.makeText(this@AuthActivity, it.message, Toast.LENGTH_LONG).show()
                        Log.d("AuthActivity", "Error: ${it.message}")
                    }
                }
            }
        }
    }

    private fun saveRole(role: String) {
        Log.d("AuthActivity", "role: $role")
        when (role) {
            UserRole.ADMIN_ROLE.name -> {
                authManager.saveRole(UserRole.ADMIN_ROLE.name)
                navToMainActivity()
            }

            UserRole.MASTER_ROLE.name -> {
                authManager.saveRole(UserRole.MASTER_ROLE.name)
                navToMainActivity()
            }

            UserRole.USER_ROLE.name -> {
                authManager.saveRole(UserRole.USER_ROLE.name)
                navToMainActivity()
            }
        }

    }

    private fun navToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}