package com.example.myrouteoptimization.ui.detail

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myrouteoptimization.data.source.remote.response.DataRouteResultsItem
import com.example.myrouteoptimization.databinding.ItemRowDetailBinding

class DetailAdapter : ListAdapter<DataRouteResultsItem, DetailAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val route = getItem(position)
        holder.bind(route)
    }

    class MyViewHolder(private val binding: ItemRowDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(route: DataRouteResultsItem){
            binding.tvVehicle.text = "Vehicle ${route.vehicleSequence?.plus(1)}"

            val detail = route.dataDetailRouteRoute!!
            for (i in detail.indices) {
                if (i == 0) {
                    binding.tvDesc.text =
                        binding.tvDesc.text.toString() + "${i + 1}. ${detail[i]?.street}, ${detail[i]?.city}, ${detail[i]?.province}, ${detail[i]?.postalCode}, Depot \n"
                } else if (i == detail.lastIndex) {
                    binding.tvDesc.text =
                        binding.tvDesc.text.toString() + "${i + 1}. ${detail[i]?.street}, ${detail[i]?.city}, ${detail[i]?.province}, ${detail[i]?.postalCode}, Depot \n"
                } else {
                    binding.tvDesc.text =
                        binding.tvDesc.text.toString() + "${i + 1}. ${detail[i]?.street}, ${detail[i]?.city}, ${detail[i]?.province}, ${detail[i]?.postalCode}, ${detail[i]?.demand} kg \n"
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataRouteResultsItem>() {
            override fun areItemsTheSame(oldItem: DataRouteResultsItem, newItem: DataRouteResultsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataRouteResultsItem, newItem: DataRouteResultsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}