package com.example.smarthomeapp.ui.shared.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.ui.shared.auth.AuthActivity
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentRegisterBinding
import com.example.smarthomeapp.domain.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignup.setOnClickListener {
            if (allFieldsAreFilled()) {
                register()
            } else Toast.makeText(
                requireContext(),
                getString(R.string.fill_upFields),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun register() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val rePassword = binding.editTextRePassword.text.toString()
        val address = binding.editTextAddress.text.toString()
        val phoneNumber = binding.editTextRePhoneNumber.text.toString()
        val fullName = binding.editTextFullName.text.toString()
        if (password == rePassword) {
            val newUser = User(
                email = email,
                address = address,
                phoneNumber = phoneNumber,
                role = "user",
                fullName = fullName,
            )
            lifecycleScope.launch {
                binding.buttonSignup.visibility = View.GONE
                viewModel.register(newUser, password)
                viewModel.registerState.collect {
                    if (it == "Done") {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.registration_is_done),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(requireActivity(), AuthActivity::class.java))
                        requireActivity().finish()
                    } else {
                        binding.buttonSignup.visibility = View.VISIBLE
                        if (it != "") Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                            .show()
                        Log.d("Register", it)
                    }
                }
            }

        } else Toast.makeText(requireContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show()
    }

    private fun allFieldsAreFilled(): Boolean {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val rePassword = binding.editTextRePassword.text.toString()
        val address = binding.editTextAddress.text.toString()
        val phoneNumber = binding.editTextRePhoneNumber.text.toString()
        val fullName = binding.editTextFullName.text.toString()

        return !(email.isEmpty() || password.isEmpty() || rePassword.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || fullName.isEmpty())

    }

}