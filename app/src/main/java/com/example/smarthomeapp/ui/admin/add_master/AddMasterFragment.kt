package com.example.smarthomeapp.ui.admin.add_master

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.UserRole
import com.example.smarthomeapp.databinding.FragmentAddMasterBinding
import com.example.smarthomeapp.domain.model.User
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddMasterFragment : DialogFragment() {
    private var _binding: FragmentAddMasterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddMasterViewModel by viewModels()
    private var password = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddMasterBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this.activity)
        builder.run { setView(binding.root) }

        binding.btnAddMaster.setOnClickListener {
            val fullName = binding.etAddMasterFullName.text.toString().trim()
            val phoneNumber = binding.etAddMasterPhoneNumber.text.toString().trim()
            val email = binding.etAddMasterEmail.text.toString().trim()
            password = binding.etAddMasterPassword.text.toString().trim()

            if (fullName.isEmpty()) {
                binding.etAddMasterFullName.error = "Name cannot be empty"
            } else if (phoneNumber.isEmpty()) {
                binding.etAddMasterPhoneNumber.error = "Surname cannot be empty"
            } else if (email.isEmpty()) {
                binding.etAddMasterEmail.error = "Email cannot be empty"
            } else if (password.isEmpty()) {
                binding.etAddMasterPassword.error = "Password cannot be empty"
            } else {
                val user = User(
                    email = email,
                    fullName = fullName,
                    role = UserRole.MASTER_ROLE.name,
                    phoneNumber = phoneNumber,
                )
                checkIfMasterExists(user)
            }
        }
        return builder.create()
    }

    private fun checkIfMasterExists(user: User) {
        lifecycleScope.launch {
            if (viewModel.masterExists(user)) Toast.makeText(
                requireContext(),
                R.string.user_exists,
                Toast.LENGTH_SHORT
            ).show()
            else saveMaster(user)
        }
    }

    private fun saveMaster(user: User) {
        lifecycleScope.launch {
            viewModel.register(user, password)
            viewModel.addMasterState.collect { state ->
                when (state) {

                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        requireContext(),
                        R.string.register_failed,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> {
                        if (state.data == "Done") {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.registration_is_done),
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                    }

                }
            }
        }
    }

}