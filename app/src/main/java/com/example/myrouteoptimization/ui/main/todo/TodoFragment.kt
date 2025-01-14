package com.example.myrouteoptimization.ui.main.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrouteoptimization.R
import com.example.myrouteoptimization.databinding.FragmentTodoBinding
import com.example.myrouteoptimization.ui.RouteViewModelFactory
import com.example.myrouteoptimization.ui.addroute.AddRouteActivity
import com.example.myrouteoptimization.utils.Result
import com.example.myrouteoptimization.utils.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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

    private val routeAdapter = TodoAdapter()

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

        observeData()

        binding.ivAddRoute.setOnClickListener {
            val intent = Intent(requireContext(), AddRouteActivity::class.java)
            startActivity(intent)
        }

        binding.rvRoute.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = routeAdapter
        }

        binding.refresh.setOnClickListener {
            observeData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        if (::mMap.isInitialized) {
            mMap.clear()
        }

        viewModel.getUnfinishedRoute().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar2.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar2.visibility = View.GONE
                        binding.error.visibility = View.GONE
                        val data = result.data
                        routeAdapter.submitList(data)

                        val firstData = data.first()
                        val dataRoute = firstData.dataRouteResults

                        binding.tvRouteTitle.text = firstData.title
                        binding.routeDistance.text =
                            "${NumberFormat
                                .getNumberInstance(Locale("id", "ID"))
                                .format(firstData.totalDistance)} km"

                        if (dataRoute != null) {
                            val boundsBuilder = LatLngBounds.Builder()

                            val depot = dataRoute[0]!!.dataDetailRouteRoute?.get(0)!!
                            val depotLatLng =
                                LatLng(
                                    depot.latitude!!.toDouble(),
                                    depot.longitude!!.toDouble()
                                )
                            boundsBuilder.include(depotLatLng)

                            mMap.addMarker(
                                MarkerOptions()
                                    .position(depotLatLng)
                                    .title("${depot.street}, ${depot.city}, ${depot.province}, ${depot.postalCode}")
                                    .snippet("Depot")
                            )

                            val hueStep = 30
                            var currentHue = 240f

                            for (i in dataRoute.indices) {
                                val newColor = Color.HSVToColor(
                                    floatArrayOf(currentHue, 1.0f, 1.0f)
                                )
                                currentHue = (currentHue + hueStep) % 360

                                val latlng = dataRoute[i]!!.dataDetailRouteRoute
                                val waypoints = mutableListOf<LatLng>()

                                for (j in 1.. latlng!!.size - 2) {
                                    val currentLatLng = LatLng(latlng[j]!!.latitude!!.toDouble(), latlng[j]!!.longitude!!.toDouble())
                                    boundsBuilder.include(currentLatLng)

                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(currentLatLng)
                                            .title("${latlng[j]?.street}, ${latlng[j]?.city}, ${latlng[j]?.province}, ${latlng[j]?.postalCode}")
                                            .snippet("${dataRoute[i]?.vehicleSequence?.plus(1)}, ${latlng[j]?.demand} kg")
                                    )

                                    waypoints.add(currentLatLng)
                                }

                                val bounds: LatLngBounds = boundsBuilder.build()
                                mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngBounds(
                                        bounds,
                                        resources.displayMetrics.widthPixels,
                                        resources.displayMetrics.heightPixels,
                                        50
                                    )
                                )

                                viewModel.getRoute(depotLatLng, depotLatLng, waypoints).observe(viewLifecycleOwner) { resultRoute ->
                                    if (resultRoute != null) {
                                        when (resultRoute) {
                                            is Result.Loading -> {
                                                binding.progressBar.visibility = View.VISIBLE
                                            }
                                            is Result.Success -> {
                                                val routeData = resultRoute.data

                                                if (routeData.isNotEmpty()) {
                                                    mMap.addPolyline(
                                                        PolylineOptions()
                                                            .addAll(routeData)
                                                            .color(newColor)
                                                            .width(8f)
                                                    )
                                                }
                                            }
                                            is Result.Error -> {
                                                binding.progressBar.visibility = View.GONE
                                                showToast(requireContext(), resultRoute.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        binding.progressBar.visibility = View.GONE
                    }
                    is Result.Error -> {
                        binding.progressBar2.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}