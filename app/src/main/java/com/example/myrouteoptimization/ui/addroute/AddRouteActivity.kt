package com.example.myrouteoptimization.ui.addroute

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrouteoptimization.data.source.remote.response.PostDataItem
import com.example.myrouteoptimization.databinding.ActivityAddRouteBinding
import com.example.myrouteoptimization.ui.adddestination.AddDestinationActivity
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
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class AddRouteActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddRouteBinding
    private lateinit var adapter: AddRouteAdapter
    private val destinationData = mutableListOf<PostDataItem>()
    private var gMaps: GoogleMap? = null

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
            addDestinationLauncher.launch(intent)
        }

        binding.optimizeRoute.setOnClickListener {

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
            }
        }
    }

    private fun setupRv() {
        adapter = AddRouteAdapter(destinationData) { pos ->
            destinationData.removeAt(pos)
            adapter.notifyItemRemoved(pos)
            updateOptimizeButtonState()
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
            val address = listOfNotNull(
                item.street,
                item.city,
                item.province,
                item.postalCode
            ).joinToString(", ")

            getLatLngFromAddress(address) { latLng ->
                latLng?.let {
                    gMaps?.addMarker(
                        MarkerOptions()
                            .position(it)
                            .title(item.street)
                    )
                    gMaps?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12f))
                }
            }
        }
    }

    private fun getLatLngFromAddress(address: String, callback: (LatLng?) -> Unit) {
        // Google Maps API Key
        val key = "AIzaSyDA8Gms6H15jvdrLusxTNj-xq92O80W8NU"
        val encodedAddress = URLEncoder.encode(address, "UTF-8")
        val urlToFetchAddress = "https://maps.googleapis.com/maps/api/geocode/json?address=$encodedAddress&key=$key"
        Log.d("url", urlToFetchAddress)

        Thread {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
                val request = Request.Builder().url(urlToFetchAddress).build()
                val response = client.newCall(request).execute()

                val json = JSONObject(response.body?.string() ?: "")
                val status = json.getString("status")
                if (status != "OK") {
                    runOnUiThread {
                        showToast(this@AddRouteActivity, "Error fetching location: $status")
                        callback(null)
                    }
                    return@Thread
                }

                val results = json.getJSONArray("results")
                if (results.length() == 0) {
                    runOnUiThread {
                        showToast(this@AddRouteActivity, "Address not found.")
                        callback(null)
                    }
                    return@Thread
                }

                val location = results.getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location")
                val lat = location.getDouble("lat")
                val lng = location.getDouble("lng")
                runOnUiThread { callback(LatLng(lat, lng)) }
            } catch (e: Exception) {
                runOnUiThread {
                    showToast(this@AddRouteActivity, "Failed to fetch location.")
                    callback(null)
                }
            }
        }.start()
    }

    private fun updateOptimizeButtonState() {
        binding.optimizeRoute.isEnabled = destinationData.size > 2
    }
}
