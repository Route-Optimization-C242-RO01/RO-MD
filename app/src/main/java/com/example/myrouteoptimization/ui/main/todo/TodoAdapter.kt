package com.example.myrouteoptimization.ui.main.todo

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
import com.example.myrouteoptimization.data.source.remote.response.DataItem
import com.example.myrouteoptimization.databinding.ItemRowRouteBinding
import com.example.myrouteoptimization.ui.detail.DetailActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.NumberFormat
import java.util.Locale

class TodoAdapter : ListAdapter<DataItem, TodoAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val route = getItem(position)
        holder.bind(route)
        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_ID, route.idResults)
            intentDetail.putExtra(DetailActivity.EXTRA_STATUS, route.status)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: ItemRowRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val mapView: MapView = itemView.findViewById(R.id.map)
        @SuppressLint("SetTextI18n")
        fun bind(route: DataItem){
            binding.tvRouteTitle.text = route.title
            binding.tvRouteDesc.text =
                "Number of vehicles: ${route.numberOfVehicles} \nTotal Distance: ${
                    NumberFormat.getNumberInstance(
                        Locale("id", "ID")
                    ).format(route.totalDistance)
                } km"
            mapView.onCreate(null)
            val dataRoute = route.dataRouteResults

            if (dataRoute != null) {
                val depot = dataRoute[0]!!.dataDetailRouteRoute?.get(0)!!
                val depotLatLng =
                    LatLng(
                        depot.latitude!!.toDouble(),
                        depot.longitude!!.toDouble()
                    )

                mapView.getMapAsync { googleMap ->
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            depotLatLng,
                            0f
                        )
                    )

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(depotLatLng)
                    )
                }

                for (i in dataRoute.indices) {
                    val latlng = dataRoute[i]!!.dataDetailRouteRoute

                    mapView.getMapAsync { googleMap ->
                        val polylineOptions = PolylineOptions()

                        polylineOptions.add(depotLatLng)

                        for (j in 1 .. latlng!!.size - 2) {
                            val currentLatLng =
                                LatLng(
                                    latlng[j]!!.latitude!!.toDouble(),
                                    latlng[j]!!.longitude!!.toDouble()
                                )

                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(currentLatLng)
                            )

                            polylineOptions.add(currentLatLng)
                        }

                        polylineOptions.add(depotLatLng)

                        googleMap.addPolyline(
                            polylineOptions
                                .color(Color.BLUE)
                                .width(8f)
                        )
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}