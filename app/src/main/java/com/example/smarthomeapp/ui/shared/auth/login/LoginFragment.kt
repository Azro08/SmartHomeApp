package com.example.smarthomeapp.ui.shared.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.MainActivity
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.databinding.FragmentLoginBinding
import com.example.smarthomeapp.util.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var authManager: AuthManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonLogin.setOnClickListener {
            if (allFieldsAreFilled()) login()
            else Toast.makeText(
                requireContext(),
                getString(R.string.fill_upFields),
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }

    private fun login() {
        binding.buttonLogin.isClickable = false
        lifecycleScope.launch {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            viewModel.login(email, password)
            viewModel.loggedIn.collect { result ->
                if (result == UserRole.ADMIN_ROLE.name || result == UserRole.USER_ROLE.name || result == UserRole.MASTER_ROLE.name) {
                    authManager.saveUer(email)
                    authManager.saveRole(result)
                    navToMainActivity()
                } else {
                    Log.d("loginErr", result)
                    if (result != "") Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT)
                        .show()
                    binding.buttonLogin.isClickable = true
                }
            }
        }
    }

    private fun navToMainActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun allFieldsAreFilled(): Boolean {
        return !(binding.editTextEmail.text.toString()
            .isEmpty() || binding.editTextPassword.text.toString().isEmpty())
    }

}