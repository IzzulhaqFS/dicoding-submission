package com.dicoding.theozu.mygithubuser2.ui.favorite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.theozu.mygithubuser2.R
import com.dicoding.theozu.mygithubuser2.adapter.FavoriteUserAdapter
import com.dicoding.theozu.mygithubuser2.database.FavoriteUser
import com.dicoding.theozu.mygithubuser2.databinding.ActivityFavoriteUserBinding
import com.dicoding.theozu.mygithubuser2.helper.ViewModelFactory
import com.dicoding.theozu.mygithubuser2.ui.detailuser.DetailUserActivity
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingPreferences
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingsActivity

class FavoriteUserActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var viewModel: FavoriteUserViewModel
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        val preferences = SettingPreferences.getInstance(dataStore)
        viewModel = obtainViewModel(this@FavoriteUserActivity, preferences)

        viewModel.getAllFavoriteUsers().observe(this) {
            setListFavorite(it)
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
        inflater.inflate(R.menu.favorite_options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this@FavoriteUserActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun setListFavorite(listUser: List<FavoriteUser>) {
        binding.apply {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            adapter = FavoriteUserAdapter(listUser)
            rvFavorite.adapter = adapter

            adapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: FavoriteUser) {
                    val intentToDetail = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                    intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    intentToDetail.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(intentToDetail)
                }
            })
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity, preferences: SettingPreferences): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, preferences)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }

    companion object {
        private const val TITLE = "Favorite"
    }
}