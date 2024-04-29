package com.example.smarthomeapp.ui.user.order_service

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.MainActivity
import com.example.smarthomeapp.R
import com.example.smarthomeapp.data.model.Order
import com.example.smarthomeapp.databinding.FragmentOrderServiceBinding
import com.example.smarthomeapp.domain.model.User
import com.example.smarthomeapp.util.AuthManager
import com.example.smarthomeapp.util.Constants
import com.example.smarthomeapp.util.DateUtils
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderServiceFragment : Fragment(R.layout.fragment_order_service) {
    private val binding by viewBinding(FragmentOrderServiceBinding::bind)
    private val viewModel: OrderServiceViewModel by viewModels()
    private var title = ""
    private var busyTimes = ArrayList<String>()
    private var spinnerList = mutableListOf<String>()
    private var serviceId: String = ""

    @Inject
    lateinit var authManager: AuthManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        getMasters()
        serviceId = arguments?.getString(Constants.SERVICE_KEY) ?: ""
        lifecycleScope.launch {
            viewModel.getServices(serviceId)
            viewModel.services.collect{
                when (it) {
                    is ScreenState.Loading ->{}
                    is ScreenState.Error -> Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    is ScreenState.Success -> {
                        Log.d("OrderServiceFragment", it.data!!.title)
                        binding.btnFinishOrder.visibility = View.VISIBLE
                        title = it.data.title
                    }
                }
            }
        }

        binding.btnOrderDate.setOnClickListener {
            DateUtils.showDateTimePickerDialog(binding.tvOrderPickedDateAndTime, requireContext())
        }

        serviceId = arguments?.getString(Constants.SERVICE_KEY) ?: ""
    }

    private fun getMasters() {
        lifecycleScope.launch {
            viewModel.allMasters.collect { state ->
                when (state) {

                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> {
                        if (state.data != null) {
                            binding.btnFinishOrder.setOnClickListener {
                                Log.d("FinishFragment", state.data.toString())
                                finishOrder(
                                    state.data.find {

                                        ("${it.fullName}: ${it.id}") == binding.spinnerMasters.selectedItem.toString()
                                    }!!
                                )
                            }
                            setSpinner(state.data)
                        } else Toast.makeText(requireContext(), R.string.failed, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
    }

    private fun finishOrder(master: User) {
        Log.d("FinishFragment ma", master.toString())
        val pickedMasterNameAndId = binding.spinnerMasters.selectedItem.toString()
        val clComment = binding.etClientComment.text.toString()
        val address = binding.etBuyerAddress.text.toString()
        val timePicked = binding.tvOrderPickedDateAndTime.text.toString()

        if (TextUtils.isEmpty(timePicked.trim()) || TextUtils.isEmpty(clComment.trim())) {
            // Handle empty fields error
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val busyTimesList = master.busyTimes.toMutableList()

        if (busyTimesList.contains(timePicked)) {
            // Master is busy at the selected time
            Toast.makeText(
                requireContext(),
                getString(R.string.master_is_busy_at_this_time), Toast.LENGTH_SHORT
            ).show()
        } else {
            // Master is available, proceed with order submission
            val order = Order(
                id = Constants.generateRandomId(),
                serviceId = serviceId,
                buyer = authManager.getUser(),
                phoneNum = binding.etBuyerPhoneNum.text.toString(),
                orderTime = timePicked,
                serviceTitle = title,
                clientComment = clComment,
                pickedMaster = pickedMasterNameAndId,
                address = address
            )

            busyTimesList.add(timePicked)

            lifecycleScope.launch {
                viewModel.updateMastersBusyTimes(
                    busyTimes, master.id
                )
                viewModel.addOrder(order)
                viewModel.addOrderState.collect { state ->
                    when (state) {
                        is ScreenState.Loading -> {}
                        is ScreenState.Error -> Toast.makeText(
                            requireContext(),
                            state.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        is ScreenState.Success -> {
                            Toast.makeText(
                                requireContext(),
                                R.string.order_submitted,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun setSpinner(list: List<User>) {
        spinnerList.clear()
        for ((j, i) in list.withIndex()) {
            spinnerList.add(j, "${i.fullName}: ${i.id}")
        }
        val myAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            spinnerList
        )
        binding.spinnerMasters.adapter = myAdapter
        if (authManager.getUser().isEmpty() || spinnerList.isEmpty()) {
            binding.llDetails.visibility = View.GONE
            binding.tvErrOrder.visibility = View.VISIBLE
            val errorText =
                "Something went wrong! \n service no longer available or no available masters"
            binding.tvErrOrder.text = errorText
        }
    }

}
