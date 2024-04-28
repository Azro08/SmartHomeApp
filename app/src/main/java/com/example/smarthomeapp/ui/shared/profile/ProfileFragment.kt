package com.example.smarthomeapp.ui.shared.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smarthomeapp.data.model.UserDto
import com.example.smarthomeapp.databinding.FragmentProfileBinding
import com.example.smarthomeapp.ui.shared.auth.AuthActivity
import com.example.smarthomeapp.util.AuthManager
import com.example.smarthomeapp.util.ScreenState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private var userId = ""

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (firebaseAuth.currentUser?.uid.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "You are not authorized", Toast.LENGTH_SHORT).show()
            logout()
        } else {
            userId = firebaseAuth.currentUser?.uid!!
        }
        binding.buttonEditProfile.setOnClickListener {
            editProfile()
        }
        binding.buttonLogout.setOnClickListener {
            logout()
        }
        binding.textViewProfileEmail.text = authManager.getUser()
        viewModelOutputs()
    }

    private fun viewModelOutputs() {
        lifecycleScope.launch {
            viewModel.getUser(userId)
            viewModel.users.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Success -> {
                        if (state.data != null) displayProfileDetails(state.data)
                        else Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is ScreenState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun displayProfileDetails(user: UserDto) {
        binding.editTextAddress.setText(user.address)
        binding.editTextPhoneNum.setText(user.phoneNumber)
    }

    private fun areAllFieldsFilled(): Boolean {
        val address = binding.editTextAddress.text.toString()
        val phoneNumber = binding.editTextPhoneNum.text.toString()

        return !(address.isEmpty() || phoneNumber.isEmpty())

    }

    private fun editProfile() {
        binding.buttonEditProfile.visibility = View.GONE
        binding.buttonSaveProfile.visibility = View.VISIBLE
        binding.buttonSaveProfile.visibility = View.VISIBLE
        binding.editTextAddress.visibility = View.VISIBLE
        binding.editTextPhoneNum.visibility = View.VISIBLE
        binding.editTextPassword.visibility = View.VISIBLE
        binding.editTextOldPassword.visibility = View.VISIBLE
        binding.profileImage.isClickable = true

        binding.buttonSaveProfile.setOnClickListener {
            if (areAllFieldsFilled()) {
                saveUser()
            } else Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun saveUser() {
        val address = binding.editTextAddress.text.toString()
        val phoneNumber = binding.editTextPhoneNum.text.toString()
        var password = binding.editTextPassword.text.toString()
        var oldPassword = ""
        var email = ""
        if (password.isEmpty()) password = ""
        else {
            if (binding.editTextOldPassword.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Old password is required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                oldPassword = binding.editTextOldPassword.text.toString()
                email = authManager.getUser()
            }
        }

        val updatedFields = mapOf(
            "address" to address,
            "phoneNumber" to phoneNumber
        )

        lifecycleScope.launch {
            viewModel.saveUser(
                firebaseAuth.currentUser?.uid!!,
                updatedFields,
                password,
                oldPassword,
                email
            )
            viewModel.userSaved.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Success -> {
                        Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT)
                            .show()
                        binding.buttonSaveProfile.visibility = View.GONE
                        binding.editTextPassword.visibility = View.GONE
                        binding.editTextOldPassword.visibility = View.GONE
                        binding.editTextAddress.visibility = View.GONE
                        binding.editTextPhoneNum.visibility = View.GONE
                        binding.buttonEditProfile.visibility = View.VISIBLE
                    }

                    is ScreenState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun logout() {
        firebaseAuth.signOut()
        authManager.removeUser()
        authManager.removeRole()
        requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}