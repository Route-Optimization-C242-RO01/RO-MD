package com.example.myrouteoptimization.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myrouteoptimization.databinding.ActivityLoginBinding
import com.example.myrouteoptimization.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.loginButton.setOnClickListener {
            val name = binding.editTextUsername.text.toString()
            val pass = binding.editTextPassword.text.toString()

            if (name.isNotEmpty() && pass.length >= 8) {
                binding.buttonError.visibility = View.GONE

                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()

//                lifecycleScope.launch {
//                    viewModel.login(name, pass).observe(this@LoginActivity) { result ->
//                        if (result != null) {
//                            when (result) {
//                                is Result.Loading -> {
//                                    binding.progressBar.visibility = View.VISIBLE
//                                }
//
//                                is Result.Success -> {
//                                    binding.progressBar.visibility = View.GONE
//
//                                    viewModel.saveSession(UserModel(email, result.data.token!!))
//
//                                    AlertDialog.Builder(this@LoginActivity).apply {
//                                        setTitle(getString(R.string.ad_title))
//                                        setMessage(result.data.name)
//                                        setPositiveButton(getString(R.string.ad_button)) { _, _ ->
//                                            val intent = Intent(context, MainActivity::class.java)
//                                            intent.flags =
//                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                        create()
//                                        show()
//                                    }
//                                }
//
//                                is Result.Error -> {
//                                    binding.progressBar.visibility = View.GONE
//                                    AlertDialog.Builder(this@LoginActivity).apply {
//                                        setTitle(getString(R.string.ad_title))
//                                        setMessage(result.error)
//                                        setPositiveButton(getString(R.string.ad_button)) { _, _ ->
//                                        }
//                                        create()
//                                        show()
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
            } else {
                binding.buttonError.visibility = View.VISIBLE
            }
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.usernameTextView, View.ALPHA, 1f).setDuration(100)
        val editTextUsernameLayout =
            ObjectAnimator.ofFloat(binding.layoutUsername, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val editTextPasswordLayout =
            ObjectAnimator.ofFloat(binding.layoutPassword, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                editTextUsernameLayout,
                passwordTextView,
                editTextPasswordLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}