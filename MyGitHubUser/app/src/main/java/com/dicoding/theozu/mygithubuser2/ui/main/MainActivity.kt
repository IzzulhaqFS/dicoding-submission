package com.dicoding.theozu.mygithubuser2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.theozu.mygithubuser2.R
import com.dicoding.theozu.mygithubuser2.ui.detailuser.DetailUserActivity
import com.dicoding.theozu.mygithubuser2.adapter.ListUserAdapter
import com.dicoding.theozu.mygithubuser2.api.ItemsItem
import com.dicoding.theozu.mygithubuser2.databinding.ActivityMainBinding
import com.dicoding.theozu.mygithubuser2.helper.ViewModelFactory
import com.dicoding.theozu.mygithubuser2.ui.favorite.FavoriteUserActivity
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingPreferences
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        val preferences = SettingPreferences.getInstance(dataStore)
        viewModel = obtainViewModel(this@MainActivity, preferences)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        binding.apply {
            rvUser.setHasFixedSize(true)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String): Boolean {
                    viewModel.setListUser(p0)
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
        }

        viewModel.listUser.observe(this) {
            setListUser(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.getThemeSettings().observe(this) { isNightModeActive: Boolean ->
            if (isNightModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu -> {
                val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun setListUser(listUser: List<ItemsItem>) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        }
        else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }
        val adapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                startActivity(intentToDetail)
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity, preferences: SettingPreferences): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, preferences)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TITLE = "Search User"
    }
}