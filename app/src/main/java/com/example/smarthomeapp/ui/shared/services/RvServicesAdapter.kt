package com.example.smarthomeapp.ui.shared.services

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.data.model.Service
import com.example.smarthomeapp.databinding.ServiceViewViewholderBinding

class RvServicesAdapter
    (
    private var servicesList: List<Service>,
    private val listener: (service: Service) -> Unit
) : RecyclerView.Adapter<RvServicesAdapter.MyViewHolder>() {

    class MyViewHolder(
        listener: (service: Service) -> Unit,
        var binding: ServiceViewViewholderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var service: Service? = null
        fun bind(myService: Service) {
            binding.tvServiceTitle.text = myService.title
            val price = myService.price.toString() + " BYN"
            binding.tvServicePrice.text = price
            service = myService
        }

        init {
            binding.ll.setOnClickListener { listener(service!!) }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateServicesList(newServicesList: List<Service>) {
        servicesList = newServicesList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            listener,
            ServiceViewViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(servicesList[position])
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }

}