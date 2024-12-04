package com.example.myrouteoptimization.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrouteoptimization.R
import com.example.myrouteoptimization.databinding.ActivityDetailBinding
import com.example.myrouteoptimization.ui.RouteViewModelFactory
import com.example.myrouteoptimization.utils.Result
import com.example.myrouteoptimization.utils.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailBinding

    private lateinit var factory: RouteViewModelFactory
    private val viewModel: DetailViewModel by viewModels {
        factory
    }

    private lateinit var id: String
    private lateinit var status: String

    private lateinit var mMap: GoogleMap

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = RouteViewModelFactory.getInstanceRoute(this@DetailActivity)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        setupView()

        id = intent.getStringExtra(EXTRA_ID)!!
        status = intent.getStringExtra(EXTRA_STATUS)!!

        if (status == "finished") {
            binding.done.visibility = View.GONE
        }

        val routeAdapter = DetailAdapter()

        binding.rvListDestination.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = routeAdapter
        }

        viewModel.getDetailRoute(id, status).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = result.data
                        val dataRoute = data.dataRouteResults

                        binding.tvRouteTitle.text = data.title
                        binding.routeDistance.text =
                            "${NumberFormat
                                .getNumberInstance(Locale("id", "ID"))
                                .format(data.totalDistance)} km"

                        routeAdapter.submitList(dataRoute)

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

                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(currentLatLng)
                                            .title("${latlng[j]?.street}, ${latlng[j]?.city}, ${latlng[j]?.province}, ${latlng[j]?.postalCode}")
                                            .snippet("${dataRoute[i]?.vehicleSequence?.plus(1)}, ${latlng[j]?.demand} kg")
                                    )

                                    waypoints.add(currentLatLng)
                                }

                                viewModel.getRoute(depotLatLng, depotLatLng, waypoints).observe(this) { resultRoute ->
                                    if (resultRoute != null) {
                                        when (resultRoute) {
                                            is Result.Loading -> {
                                                binding.progressBar.visibility = View.VISIBLE
                                            }
                                            is Result.Success -> {
                                                binding.progressBar.visibility = View.GONE
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
                                                showToast(this, resultRoute.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        binding.done.setOnClickListener {
                            setupAction()
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this, result.error)
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

    private fun setupAction() {
        viewModel.updateRoute(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this, result.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this, result.error)
                    }
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_STATUS = "extra_status"
    }
}