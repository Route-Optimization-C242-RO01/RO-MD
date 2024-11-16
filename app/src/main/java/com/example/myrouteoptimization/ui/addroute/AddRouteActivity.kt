package com.example.myrouteoptimization.ui.addroute

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myrouteoptimization.databinding.ActivityAddRouteBinding
import com.example.myrouteoptimization.ui.adddestination.AddDestinationActivity

class AddRouteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRouteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.addDestination.setOnClickListener{
            val intent = Intent(this, AddDestinationActivity::class.java)
            startActivity(intent)
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
}