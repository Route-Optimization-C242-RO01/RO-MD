package com.example.myrouteoptimization.ui.adddestination

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myrouteoptimization.data.source.remote.response.PostDataItem
import com.example.myrouteoptimization.databinding.ActivityAddDestinationBinding
import com.example.myrouteoptimization.ui.addroute.AddRouteActivity
import com.example.myrouteoptimization.utils.showToast

@Suppress("DEPRECATION")
class AddDestinationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDestinationBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val isFirstInput = intent.getBooleanExtra(AddRouteActivity.IS_FIRST_INPUT, true)

        binding.apply {
            if(isFirstInput) {
                editTextKg.apply {
                    setText("0")
                    isEnabled = false
                }
                warningMessageDepot.visibility = View.VISIBLE
            } else {
                layoutKg.visibility = View.VISIBLE
                warningMessageDepot.text = "Note: The maximum capacity for each vehicle is 120 kg.\n"
            }
        }

        binding.submit.setOnClickListener {
            val street = binding.editTextStreet.text.toString()
            val city = binding.editTextCity.text.toString()
            val province = binding.editTextProvince.text.toString()
            val postalCode = binding.editTextPostalCode.text.toString()
            val kg = binding.editTextKg.text.toString()

            if (street.isNotBlank() && city.isNotBlank() && province.isNotBlank() && postalCode.isNotBlank()) {

                val kgValue = kg.toIntOrNull()

                binding.layoutKg.apply {
                    if (kgValue != null && kgValue > 120) {
                        error = "Vehicle Capacity must have less than or equals 120kg"
                        return@setOnClickListener
                    } else {
                        error = null
                    }
                }

                val itemData = PostDataItem(
                    street = street,
                    city = city,
                    province = province,
                    postalCode = postalCode,
                    kg = kg
                )

                setResult(RESULT_OK, Intent().putExtra("EXTRA_DESTINATION", itemData))
                finish()
            } else {
                showToast(this@AddDestinationActivity, "Input is not Completed")
                if (binding.editTextStreet.text!!.isBlank()) {
                    binding.layoutStreet.error = "Street is required"
                 }
                if (binding.editTextCity.text!!.isBlank()) {
                    binding.layoutCity.error = "City is required"
                }
                if (binding.editTextProvince.text!!.isBlank()) {
                    binding.layoutProvince.error = "Province is required"
                }
                if (binding.editTextPostalCode.text!!.isBlank()) {
                    binding.layoutPostalCode.error = "Postal Code is required"
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
}