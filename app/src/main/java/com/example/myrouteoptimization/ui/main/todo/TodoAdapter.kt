package com.example.myrouteoptimization.ui.main.todo

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrouteoptimization.R
import com.example.myrouteoptimization.data.source.remote.response.DataItem
import com.example.myrouteoptimization.databinding.ItemRowRouteBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class TodoAdapter : ListAdapter<DataItem, TodoAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val route = getItem(position)
        holder.bind(route)
//        holder.itemView.setOnClickListener{
//            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
//            intentDetail.putExtra(DetailActivity.EXTRA_STORY, route.id)
//            holder.itemView.context.startActivity(intentDetail)
//        }
    }

    class MyViewHolder(private val binding: ItemRowRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val mapView: MapView = itemView.findViewById(R.id.map)
        @SuppressLint("SetTextI18n")
        fun bind(route: DataItem){
            binding.tvRouteTitle.text = route.title
            binding.tvRouteDesc.text = "Number of vehicles: ${route.numberOfVehicles}"
            mapView.onCreate(null)
            val dataRoute = route.dataRouteResults

            if (dataRoute != null) {
                mapView.getMapAsync { googleMap ->
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                -6.176274,
                                106.791580
                            ), 0f
                        )
                    )

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(-6.176274, 106.791580))
                            .title("Gudang")
                    )
                }

                for (i in dataRoute.indices) {
                    val latlng = dataRoute[i]!!.dataDetailRouteRoute

                    mapView.getMapAsync { googleMap ->
                        val polylineOptions = PolylineOptions()

                        polylineOptions.add(LatLng(-6.176274, 106.791580))
                        for (j in latlng!!.indices) {
                            val currentLatLng =
                                LatLng(
                                    latlng[j]!!.latitude!!.toDouble(),
                                    latlng[j]!!.longitude!!.toDouble()
                                )

                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(currentLatLng)
                                    .title("${latlng[j]?.street}, ${latlng[j]?.city}, ${latlng[j]?.province}, ${latlng[j]?.postalCode}")
                            )

                            polylineOptions.add(currentLatLng)
                        }
                        polylineOptions.add(LatLng(-6.176274, 106.791580))

                        googleMap.addPolyline(
                            polylineOptions
                                .color(Color.BLUE)
                                .width(5f)
                        )
                        Log.d("Abc", "Polyline")
                    }
                }

                mapView.getMapAsync { googleMap ->
                    googleMap.addPolyline(
                        PolylineOptions()
                            .add(LatLng(-6.176274, 106.791580))
                            .color(Color.BLUE)
                            .width(5f)
                    )
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