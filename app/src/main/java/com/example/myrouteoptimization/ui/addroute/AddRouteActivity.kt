package com.example.myrouteoptimization.ui.addroute

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrouteoptimization.data.source.remote.response.OptimizeRequest
import com.example.myrouteoptimization.data.source.remote.response.PostDataItem
import com.example.myrouteoptimization.databinding.ActivityAddRouteBinding
import com.example.myrouteoptimization.ui.RouteViewModelFactory
import com.example.myrouteoptimization.ui.adddestination.AddDestinationActivity
import com.example.myrouteoptimization.utils.Result
import com.example.myrouteoptimization.utils.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("DEPRECATION")
class AddRouteActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddRouteBinding
    private lateinit var adapter: AddRouteAdapter
    private val destinationData = mutableListOf<PostDataItem>()
    private var gMaps: GoogleMap? = null
    private val viewModel : AddRouteViewModel by viewModels {
        RouteViewModelFactory.getInstanceRoute(this@AddRouteActivity)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRv()
        setupMap()
        updateOptimizeButtonState()

        binding.addDestination.setOnClickListener {
            val intent = Intent(this, AddDestinationActivity::class.java)
            intent.putExtra(IS_FIRST_INPUT, destinationData.isEmpty())
            addDestinationLauncher.launch(intent)
        }

        binding.optimizeRoute.setOnClickListener {
            val title = binding.editTextRouteTitle.text.toString().trim()
            val vehicleCountText = binding.editTextRouteVehicles.text.toString().trim()

            if (title.isBlank()) {
                showToast(this, "Title Input must be Filled!")
                return@setOnClickListener
            }

            if (vehicleCountText.isBlank()) {
                showToast(this, "Please enter the number of vehicles!")
                return@setOnClickListener
            }

            val vehicleCount = try {
                vehicleCountText.toInt()
            } catch (e: NumberFormatException) {
                showToast(this, "Invalid number format for vehicles!")
                return@setOnClickListener
            }

            val request = OptimizeRequest(
                numberOfVehicles = vehicleCount,
                title = title,
                data = destinationData
            )

            viewModel.optimizeRoute(request).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarOptimize.visibility = View.VISIBLE
                        showToast(this@AddRouteActivity, "Optimizing Route....")
                    }
                    is Result.Success -> {
                        binding.progressBarOptimize.visibility = View.GONE
                        showToast(this@AddRouteActivity, "Success Optimize Route")
                        destinationData.clear()
                        adapter.notifyDataSetChanged()
                        finish()
                    }
                    is Result.Error -> {
                        binding.progressBarOptimize.visibility = View.GONE

                        showToast(this@AddRouteActivity, "Failed to optimize route because ${result.error}")
                    }
                }
            }
        }
    }

    private val addDestinationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == RESULT_OK) {
            res.data?.let {
                val newRoute = it.getParcelableExtra<PostDataItem>("EXTRA_DESTINATION")
                if (newRoute != null) {
                    destinationData.add(newRoute)
                    adapter.notifyItemInserted(destinationData.size - 1)
                    updateOptimizeButtonState()
                    updateMapMarkers()
                }
            }
        }
    }

    private fun setupRv() {
        adapter = AddRouteAdapter(
            destinationList = destinationData,
            onDeleteClick = { pos ->
                if (pos >= 0 && pos < destinationData.size) {
                    destinationData.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    updateOptimizeButtonState()
                    updateMapMarkers()
                }
            },
            onItemClicked = { item ->
                val address = "${item.street}, ${item.city}, ${item.postalCode}"
                val latLngData = getLatLngFromAddress(this, address)

                latLngData?.let {
                    gMaps?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
                }
            }
        )

        binding.rvListDestination.layoutManager = LinearLayoutManager(this)
        binding.rvListDestination.adapter = adapter
    }

    private fun setupView() {
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

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        gMaps = p0
        updateMapMarkers()
    }

    private fun updateMapMarkers() {
        for ((index, item) in destinationData.withIndex()) {
            val address = "${item.street}, ${item.city}, ${item.postalCode}"

            val latLngData = getLatLngFromAddress(this, address)
            latLngData?.let {
                val markerOptions = MarkerOptions()
                    .position(it)
                    .title(item.street)

                if(index == 0) markerOptions.snippet("DEPOT") else markerOptions.snippet("Destination $index")

                gMaps?.addMarker(markerOptions)

                gMaps?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 8f))
            }
        }
    }

    private fun getLatLngFromAddress(context : Context, mAddress : String) : LatLng? {
        val coder = Geocoder(context)
        return try {
            val addresses : List<Address> = coder.getFromLocationName(mAddress, 1) as List<Address>
            if(addresses.isNotEmpty()) {
                val location = addresses[0]
                LatLng(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e : Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateOptimizeButtonState() {
        binding.apply {
            dataMessage.visibility = if (destinationData.size == 0) View.VISIBLE else View.GONE
            optimizeRoute.isEnabled = destinationData.size > 2
        }
    }

    companion object {
        const val IS_FIRST_INPUT = "is_first_input"
    }
}
