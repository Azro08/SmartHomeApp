package com.example.smarthomeapp.ui.admin.edit_master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.databinding.MasterViewHolderBinding

class RvMasterAdapter(private val busyTimes: List<String>) :
    RecyclerView.Adapter<RvMasterAdapter.MasterViewHolder>() {

    class MasterViewHolder(private val binding: MasterViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var busyTime = ""
        fun bind(currentBusyTime: String) {
            binding.tvBusyTime.text = currentBusyTime
            busyTime = currentBusyTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        return MasterViewHolder(
            MasterViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return busyTimes.size
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) {
        holder.bind(busyTimes[position])
    }

}