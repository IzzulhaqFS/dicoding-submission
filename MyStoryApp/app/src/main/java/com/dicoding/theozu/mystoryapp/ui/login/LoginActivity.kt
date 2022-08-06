package com.dicoding.theozu.mystoryapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.theozu.mystoryapp.ViewModelFactory
import com.dicoding.theozu.mystoryapp.databinding.ActivityLoginBinding
import com.dicoding.theozu.mystoryapp.model.UserPreference
import com.dicoding.theozu.mystoryapp.ui.main.MainActivity
import com.dicoding.theozu.mystoryapp.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> binding.emailEditTextLayout.error = "Email cannot be empty"
                password.isEmpty() -> binding.passwordEditTextLayout.error  = "Password cannot be empty"
                else -> {
                    viewModel.login(email, password)
                    viewModel.isSuccessful.observe(this) {
                        if (it) {
                            AlertDialog.Builder(this).apply {
                                setTitle("Login Success")
                                setMessage("You're Logged In")
                                setOnCancelListener {
                                    intentToMain(context)
                                    finish()
                                }
                                setPositiveButton("OK") { _, _ ->
                                    intentToMain(context)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                    }
                }
            }
        }

        binding.btnToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBarLogin.visibility = View.VISIBLE else binding.progressBarLogin.visibility = View.GONE
    }

    private fun intentToMain(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}