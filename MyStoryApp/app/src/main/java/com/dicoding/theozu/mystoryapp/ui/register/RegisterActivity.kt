package com.dicoding.theozu.mystoryapp.ui.register

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.theozu.mystoryapp.ViewModelFactory
import com.dicoding.theozu.mystoryapp.databinding.ActivityRegisterBinding
import com.dicoding.theozu.mystoryapp.model.UserPreference
import com.dicoding.theozu.mystoryapp.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[RegisterViewModel::class.java]

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> binding.nameEditTextLayout.error = "Name cannot be empty"
                email.isEmpty() -> binding.emailEditTextLayout.error = "Email cannot be empty"
                password.isEmpty() -> binding.passwordEditTextLayout.error = "Password cannot be empty"
                else -> {
                    viewModel.register(name, email, password)
                    viewModel.isSuccessful.observe(this) {
                        if (it) {
                            AlertDialog.Builder(this).apply {
                                setTitle("Register success")
                                setMessage("Register is success. Login to continue")
                                setPositiveButton("Next") { _, _ ->
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                setOnCancelListener {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        else {
                            AlertDialog.Builder(this).apply {
                                setTitle("Register failed")
                                setMessage("Register is failed. Try again")
                                setNeutralButton("OK") { _, _ ->
                                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                                }
                                create()
                                show()
                            }
                        }
                    }
                }
            }
        }

        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBarRegister.visibility = View.VISIBLE else binding.progressBarRegister.visibility = View.GONE
    }
}