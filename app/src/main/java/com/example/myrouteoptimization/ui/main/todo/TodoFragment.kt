package com.example.myrouteoptimization.ui.main.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrouteoptimization.R
import com.example.myrouteoptimization.databinding.FragmentTodoBinding
import com.example.myrouteoptimization.ui.AuthViewModelFactory
import com.example.myrouteoptimization.ui.RouteViewModelFactory
import com.example.myrouteoptimization.ui.addroute.AddRouteActivity
import com.example.myrouteoptimization.ui.main.MainViewModel
import com.example.myrouteoptimization.utils.Result
import com.example.myrouteoptimization.utils.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.NumberFormat
import java.util.Locale

class TodoFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: RouteViewModelFactory
    private val viewModel: TodoViewModel by viewModels {
        factory
    }

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = RouteViewModelFactory.getInstanceRoute(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.ivAddRoute.setOnClickListener {
            val intent = Intent(requireContext(), AddRouteActivity::class.java)
            startActivity(intent)
        }

        val routeAdapter = TodoAdapter()

        binding.rvRoute.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = routeAdapter
        }

        viewModel.getUnfinishedRoute().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar2.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar2.visibility = View.GONE
                        val data = result.data
                        routeAdapter.submitList(data)
                    }
                    is Result.Error -> {
                        binding.progressBar2.visibility = View.GONE
                        binding.error.text = result.error
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        viewModel.getUnfinishedRoute().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = result.data
                        val firstData = data.first()
                        val dataRoute = firstData.dataRouteResults

                        binding.tvRouteTitle.text = firstData.title
                        binding.routeDistance.text = "${NumberFormat.getNumberInstance(Locale("id", "ID")).format(firstData.totalDistance)} km"

                        if (dataRoute != null) {
                            val depot = dataRoute[0]!!.dataDetailRouteRoute?.get(0)!!
                            val depotLatLng =
                                LatLng(
                                    depot.latitude!!.toDouble(),
                                    depot.longitude!!.toDouble()
                                )

                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    depotLatLng,
                                    5f
                                )
                            )

                            mMap.addMarker(
                                MarkerOptions()
                                    .position(depotLatLng)
                                    .title("${depot.street}, ${depot.city}, ${depot.province}, ${depot.postalCode}")
                                    .snippet("Depot")
                            )

                            for (i in dataRoute.indices) {
                                val latlng = dataRoute[i]!!.dataDetailRouteRoute

                                val polylineOptions = PolylineOptions()

                                polylineOptions.add(depotLatLng)

                                for (j in 1 .. latlng!!.size - 2) {
                                    val currentLatLng =
                                        LatLng(
                                            latlng[j]!!.latitude!!.toDouble(),
                                            latlng[j]!!.longitude!!.toDouble()
                                        )

                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(currentLatLng)
                                            .title("${latlng[j]?.street}, ${latlng[j]?.city}, ${latlng[j]?.province}, ${latlng[j]?.postalCode}")
                                            .snippet("${dataRoute[i]?.vehicleSequence}, ${latlng[j]?.demand} kg")
                                    )

                                    polylineOptions.add(currentLatLng)
                                }

                                polylineOptions.add(depotLatLng)

                                mMap.addPolyline(
                                    polylineOptions
                                        .color(Color.BLUE)
                                        .width(8f)
                                )

                            }
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}