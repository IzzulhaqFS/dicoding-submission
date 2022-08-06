package com.dicoding.theozu.mystoryapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.theozu.mystoryapp.adapter.ListStoryAdapter
import com.dicoding.theozu.mystoryapp.R
import com.dicoding.theozu.mystoryapp.ViewModelFactory
import com.dicoding.theozu.mystoryapp.adapter.LoadingStateAdapter
import com.dicoding.theozu.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.theozu.mystoryapp.model.UserPreference
import com.dicoding.theozu.mystoryapp.ui.addstory.AddStoryActivity
import com.dicoding.theozu.mystoryapp.ui.login.LoginActivity
import com.dicoding.theozu.mystoryapp.ui.maps.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                viewModel.setListStories(user.token)
                showListStory()
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.fabMain.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                viewModel.logout()
                true
            }
            R.id.btn_maps_view -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showListStory() {
        val adapter = ListStoryAdapter()
        binding.apply {
            rvStories.setHasFixedSize(true)
            rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            viewModel.getListStories().observe(this@MainActivity) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    companion object {
        private const val TITLE = "Story"
    }
}