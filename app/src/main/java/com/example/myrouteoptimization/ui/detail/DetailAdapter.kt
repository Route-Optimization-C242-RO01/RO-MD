package com.example.myrouteoptimization.ui.detail

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myrouteoptimization.R
import com.example.myrouteoptimization.data.source.remote.response.DataRouteResultsItem
import com.example.myrouteoptimization.databinding.ItemRowDetailBinding
import com.example.myrouteoptimization.ui.detail.DetailActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.NumberFormat
import java.util.Locale

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
            binding.tvVehicle.text = "Vehicle ${route.vehicleSequence}"

            val detail = route.dataDetailRouteRoute!!
            for (i in detail.indices) {
                binding.tvDesc.text =
                    binding.tvDesc.text.toString() + "${i + 1}. ${detail[i]?.street}, ${detail[i]?.city}, ${detail[i]?.province}, ${detail[i]?.postalCode}, ${detail[i]?.demand} kg \n"
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