package com.example.smarthomeapp.ui.shared.order_history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.data.model.Order
import com.example.smarthomeapp.databinding.OrderViewholderBinding

class RvOrderAdapter
    (
    private val orderList: List<Order>
) : RecyclerView.Adapter<RvOrderAdapter.MyViewHolder>() {
    class MyViewHolder(
        var binding: OrderViewholderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var order: Order? = null
        fun bind(myOrder: Order) {
            binding.orderTitle.text = myOrder.serviceTitle
            binding.orderTime.text = myOrder.orderTime
            binding.textViewBuyerPhoneNumber.text = myOrder.phoneNum
            binding.textViewBuyerAddress.text = myOrder.address
            binding.textViewBuyerComment.text = myOrder.clientComment
            order = myOrder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            OrderViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}