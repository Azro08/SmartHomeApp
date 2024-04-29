package com.example.smarthomeapp.ui.admin.edit_master

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.smarthomeapp.R
import com.example.smarthomeapp.databinding.FragmentEditMasterBinding
import com.example.smarthomeapp.domain.model.User
import com.example.smarthomeapp.ui.admin.add_master.AddMasterFragment
import com.example.smarthomeapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditMasterFragment : Fragment(R.layout.fragment_edit_master) {
    private val binding by viewBinding(FragmentEditMasterBinding::bind)
    private val mastersList = arrayListOf("")
    private var rvAdapter: RvMasterAdapter? = null
    private val viewModel: EditMasterViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getAllMasters()
        setMenu()
        binding.btnShowMaster.setOnClickListener {
            val masterId = binding.spinnerMyMasters.selectedItem.toString()
            getMasterBusyTimes(masterId)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMasters()
        getAllMasters()
    }

    private fun setMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.itemAddNewElement -> {
                        val addMasterFragment = AddMasterFragment()
                        addMasterFragment.show(
                            requireActivity().supportFragmentManager,
                            "addMasterFragment"
                        )
                    }
                }
                return true
                // Handle option Menu Here
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getMasterBusyTimes(masterId: String) {
        lifecycleScope.launch {
            viewModel.getUser(masterId)
            viewModel.masterDetails.collect { state ->
                when (state) {

                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> {
                        showBusyTimes(state.data!!)
                    }

                }
            }
        }
    }

    private fun showBusyTimes(user: User) {
        binding.tvCurMasterName.text = user.fullName
        val masterIdName = "id: " + user.id
        binding.tvMasterId.text = masterIdName
        rvAdapter = RvMasterAdapter(user.busyTimes)
        binding.rvBusyTimes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBusyTimes.setHasFixedSize(true)
        binding.rvBusyTimes.adapter = rvAdapter
    }

    private fun getAllMasters() {
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
                        setSpinner(state.data!!)
                    }

                }
            }
        }
    }

    private fun setSpinner(masters: List<User>) {
        for (master in masters) {
            mastersList.add(master.fullName)
        }
    }
}