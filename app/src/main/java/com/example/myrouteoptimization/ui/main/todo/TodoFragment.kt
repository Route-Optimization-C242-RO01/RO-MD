package com.example.myrouteoptimization.ui.main.todo

import android.content.Intent
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = routeAdapter
        }

        viewModel.getUnfinishedRoute().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = result.data
                        routeAdapter.submitList(data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(requireContext(), result.error)
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        googleMap.setOnMapLoadedCallback {
            // Callback ini dipanggil jika peta berhasil dimuat
            Toast.makeText(requireContext(), "Map loaded successfully!", Toast.LENGTH_SHORT).show()
        }

        // Contoh menambahkan marker
        val sampleLocation = LatLng(-6.200000, 106.816666) // Koordinat Jakarta
        mMap.addMarker(MarkerOptions().position(sampleLocation).title("Marker in Jakarta"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 12f))
    }

//    override fun onResume() {
//        super.onResume()
//        binding.rvRoute.children.forEach {
//            (it.findViewById<MapView>(R.id.map))?.onResume()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        binding.rvRoute.children.forEach {
//            (it.findViewById<MapView>(R.id.map))?.onPause()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        binding.rvRoute.children.forEach {
//            (it.findViewById<MapView>(R.id.map))?.onDestroy()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}