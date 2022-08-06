package com.dicoding.theozu.mygithubuser2.ui.detailuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.theozu.mygithubuser2.R
import com.dicoding.theozu.mygithubuser2.adapter.SectionsPagerAdapter
import com.dicoding.theozu.mygithubuser2.databinding.ActivityDetailUserBinding
import com.dicoding.theozu.mygithubuser2.helper.ViewModelFactory
import com.dicoding.theozu.mygithubuser2.ui.favorite.FavoriteUserActivity
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingPreferences
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingsActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        val preferences = SettingPreferences.getInstance(dataStore)
        detailUserViewModel = obtainViewModel(this@DetailUserActivity, preferences)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        Log.d("DetailUserActivity", id.toString())
        Log.d("DetailUserActivity", username.toString())
        Log.d("DetailUserActivity", avatar.toString())
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        detailUserViewModel.setUser(username.toString())
        detailUserViewModel.user.observe(this) {
            binding.apply {
                Glide.with(this@DetailUserActivity)
                    .load(it.avatarUrl)
                    .placeholder(R.drawable.ic_person_grey)
                    .circleCrop()
                    .into(imgDetailPhoto)
                tvDetailName.text = it.name
                tvDetailUsername.text = it.login
                tvFollowersNumber.text = it.followers.toString()
                tvFollowingNumber.text = it.following.toString()
                tvRepositoryNumbers.text = it.publicRepos.toString()
                tvCompanyName.text = it.company
                tvLocationName.text = it.location
            }
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        var isChecked = false

        CoroutineScope(Dispatchers.IO).launch {
            val count = detailUserViewModel.checkFavoriteUser(id)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    binding.favoriteToggle.isChecked = true
                    isChecked = true
                }
                else {
                    binding.favoriteToggle.isChecked = false
                    isChecked = false
                }
            }
        }

        binding.favoriteToggle.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                detailUserViewModel.insert(id, username.toString(), avatar.toString())
            }
            else {
                detailUserViewModel.delete(id)
            }
            binding.favoriteToggle.isChecked = isChecked
        }

        detailUserViewModel.getThemeSettings().observe(this) { isNightModeActive ->
            if (isNightModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
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
                val intent = Intent(this@DetailUserActivity, FavoriteUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.settings -> {
                val intent = Intent(this@DetailUserActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity: AppCompatActivity, preferences: SettingPreferences): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, preferences)
        return ViewModelProvider(activity, factory).get(DetailUserViewModel::class.java)
    }

    companion object {
        const val EXTRA_USERNAME = "username"
        const val EXTRA_ID = "id"
        const val EXTRA_AVATAR = "avatar"
        private const val TITLE = "Detail User"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower_tab_name,
            R.string.following_tab_name
        )
    }
}