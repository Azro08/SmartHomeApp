package com.example.smarthomeapp.ui.user.order_service

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class OrderServiceFragment : Fragment(R.layout.fragment_order_service) {
    private val binding by viewBinding(FragmentOrderServiceBinding::bind)
    private val viewModel: OrderServiceViewModel by viewModels()
    private val timesList = listOf("10:00 - 12:00", "14:00 - 16:00", "18:00 - 19:00")
    private var title = ""
    private var busyTimes = ArrayList<String>()
    private var spinnerList = mutableListOf<String>()
    private var serviceId: String = ""

    @Inject
    lateinit var authManager: AuthManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        getMasters()

        lifecycleScope.launch {
            viewModel.getServices(serviceId)?.let {
                title = it.title
            }
        }

        val myAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            timesList
        )
        binding.spPickedTime.adapter = myAdapter

        binding.btnOrderDate.setOnClickListener {
            setDate()
        }

        serviceId = arguments?.getString(Constants.SERVICE_KEY) ?: ""
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.ivGif.visibility = View.GONE
                binding.llDetails.visibility = View.VISIBLE
                if (authManager.getUser().isEmpty() || spinnerList.isEmpty()) {
                    binding.llDetails.visibility = View.GONE
                    binding.tvErrOrder.visibility = View.VISIBLE
                    val errorText =
                        "Something went wrong! \n service no longer available or no available masters"
                    binding.tvErrOrder.text = errorText
                }
            }, 3000L
        )

    }

    private fun setDate() {
        val dialog = datePickerDialog()
        dialog.show()
        val year = dialog.findViewById<View>(
            Resources.getSystem().getIdentifier("android:id/year", null, null)
        )
        if (year != null) {
            year.visibility = View.GONE
        }
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
                        binding.btnFinishOrder.setOnClickListener {
                            Log.d("FinishFragment", "Finish")
                            finishOrder(state.data!!.find { it.fullName == binding.spinnerMasters.selectedItem.toString() }!!)
                        }
                        setSpinner(state.data!!)
                    }

                }
            }
        }
    }


    private fun finishOrder(master: User) {
        val masterName = binding.spinnerMasters.selectedItem.toString()
        val clComment = binding.etClientComment.text.toString()
        val address = binding.etBuyerAddress.text.toString()
        val date = binding.tvOrderPickedDate.text.toString()
        val hrs = binding.spPickedTime.selectedItem.toString()
        val timePicked = "$date $hrs"

        if (TextUtils.isEmpty(date.trim()) || TextUtils.isEmpty(clComment.trim())) {
            // Handle empty fields error
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val busyTimesList = master.busyTimes.toMutableList()

        if (busyTimesList.contains(timePicked)) {
            // Master is busy at the selected time
            Toast.makeText(requireContext(), "Мастер занят в это время", Toast.LENGTH_SHORT).show()
        } else {
            // Master is available, proceed with order submission
            val order = Order(
                serviceId = serviceId,
                buyer = authManager.getUser(),
                phoneNum = binding.etBuyerPhoneNum.text.toString(),
                orderTime = timePicked,
                serviceTitle = title,
                clientComment = clComment,
                pickedMaster = masterName,
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

    private fun datePickerDialog(): DatePickerDialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Material_Light_Dialog,
            { _, pickedYear, monthOfYear, dayOfMonth ->
                val dateText = "$dayOfMonth/$monthOfYear/$pickedYear"
                binding.tvOrderPickedDate.text = dateText
            },
            year,
            month,
            day
        )
        // Show Date Picker
        datePickerDialog.datePicker.minDate = (System.currentTimeMillis() - 1000)
        return datePickerDialog
    }

    private fun setSpinner(list: List<User>) {
        spinnerList.clear()
        for ((j, i) in list.withIndex()) {
            spinnerList.add(j, i.fullName)
        }
        val myAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            spinnerList
        )
        binding.spinnerMasters.adapter = myAdapter
    }

}
