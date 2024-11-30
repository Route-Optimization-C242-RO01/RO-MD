package com.example.myrouteoptimization.ui.addroute

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.myrouteoptimization.ui.main.MainActivity
import com.example.myrouteoptimization.utils.Result
import com.example.myrouteoptimization.utils.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

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
            intent.putExtra("IS_FIRST_INPUT", destinationData.isEmpty())
            addDestinationLauncher.launch(intent)
        }

        binding.optimizeRoute.setOnClickListener {
            val title = binding.editTextRouteTitle.text.toString()
            val vehicleCountText = binding.editTextRouteVehicles.text.toString()
            val vehicleCount = vehicleCountText.toInt()

            if (vehicleCount <= 0) {
                showToast(this, "Jumlah kendaraan harus ada")
            }
            val request = OptimizeRequest(
                numberOfVehicles = vehicleCount,
                title = title,
                data = destinationData
            )
            Log.d("AddRouteActivity", request.toString())

            viewModel.optimizeRoute(request).observe(this) {result ->
                when(result) {
                    is Result.Loading -> {
                        showToast(this@AddRouteActivity, "Mengoptimalkan Route")
                    }
                    is Result.Error -> {
                        showToast(this@AddRouteActivity, "Gagal optimisasi rute")
                        Log.d("Destination data : ", result.error)
                    }
                    is Result.Success -> {
                        showToast(this@AddRouteActivity, "Rute berhasil dioptimalkan")
                        destinationData.clear()
                        adapter.notifyDataSetChanged()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
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
                }
                Log.d("Destination Data", destinationData.toString())
            }
        }
    }

    private fun setupRv() {
        adapter = AddRouteAdapter(destinationData) { pos ->
            if (pos >= 0 && pos < destinationData.size) {
                destinationData.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                updateOptimizeButtonState()
            }
        }

        binding.rvListDestination.layoutManager = LinearLayoutManager(this)
        binding.rvListDestination.adapter = adapter
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

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        gMaps = p0
        updateMapMarkers()
    }

    private fun updateMapMarkers() {
        gMaps?.clear()
        for (item in destinationData) {
            val address = item.street

            getLatLngFromAddress(address) { latLng ->
                latLng?.let {
                    gMaps?.addMarker(
                        MarkerOptions()
                            .position(it)
                            .title(address)
                    )
                    gMaps?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12f))
                }
            }
        }
    }

    private fun getLatLngFromAddress(address: String, callback: (LatLng?) -> Unit) {
        val apiKey = "AIzaSyDA8Gms6H15jvdrLusxTNj-xq92O80W8NU"
        val encodedAddress = URLEncoder.encode(address, "UTF-8")
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=$encodedAddress&key=$apiKey"

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    callback(null)
                    return
                }

                response.body?.string()?.let { responseBody ->
                    try {
                        val json = JSONObject(responseBody)
                        val location = json.getJSONArray("results")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location")

                        val lat = location.getDouble("lat")
                        val lng = location.getDouble("lng")

                        callback(LatLng(lat, lng))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(null)
                    }
                }
            }
        })
    }

    private fun updateOptimizeButtonState() {
        binding.optimizeRoute.isEnabled = destinationData.size > 2
    }
}
