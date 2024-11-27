package com.example.myrouteoptimization.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myrouteoptimization.R
import com.example.myrouteoptimization.databinding.ActivityRegisterBinding
import com.example.myrouteoptimization.ui.AuthViewModelFactory
import kotlinx.coroutines.launch
import com.example.myrouteoptimization.utils.Result
import com.example.myrouteoptimization.utils.showMaterialDialog

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var factory: AuthViewModelFactory
    private val viewModel: RegisterViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = AuthViewModelFactory.getInstanceUser(this@RegisterActivity)

        setupView()
        setupAction()
        playAnimation()
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

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = binding.editTextUsername.text.toString()
            val pass = binding.editTextPassword.text.toString()
            val retypePass = binding.editTextRetypePassword.text.toString()

            if (name.isNotEmpty() && pass.length >= 8 && retypePass.length >= 8 && pass == retypePass){
                binding.buttonError.visibility = View.GONE

                lifecycleScope.launch {
                    viewModel.register(name, pass, retypePass).observe(this@RegisterActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    showMaterialDialog(
                                        this@RegisterActivity,
                                        getString(R.string.ad_title),
                                        result.data!!,
                                        getString(R.string.ad_button)
                                    ) {
                                        finish()
                                    }
                                }

                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    showMaterialDialog(
                                        this@RegisterActivity,
                                        getString(R.string.ad_title_error),
                                        result.error,
                                        getString(R.string.ad_button)
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (pass != retypePass) {
                binding.buttonError.visibility = View.VISIBLE
                binding.buttonError.text = getString(R.string.pass_error)
            } else {
                binding.buttonError.visibility = View.VISIBLE
                binding.buttonError.text = getString(R.string.button_error)
            }
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val editTextUsernameLayout =
            ObjectAnimator.ofFloat(binding.layoutUsername, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val editTextPasswordLayout =
            ObjectAnimator.ofFloat(binding.layoutPassword, View.ALPHA, 1f).setDuration(100)
        val retypePasswordTextView =
            ObjectAnimator.ofFloat(binding.retypePasswordTextView, View.ALPHA, 1f).setDuration(100)
        val editTextRetypePasswordLayout =
            ObjectAnimator.ofFloat(binding.layoutRetypePassword, View.ALPHA, 1f).setDuration(100)
        val register = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                editTextUsernameLayout,
                passwordTextView,
                editTextPasswordLayout,
                retypePasswordTextView,
                editTextRetypePasswordLayout,
                register
            )
            startDelay = 100
        }.start()
    }
}