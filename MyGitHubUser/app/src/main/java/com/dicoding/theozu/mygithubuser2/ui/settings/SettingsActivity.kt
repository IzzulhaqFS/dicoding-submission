package com.dicoding.theozu.mygithubuser2.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.theozu.mygithubuser2.databinding.ActivitySettingsBinding
import com.dicoding.theozu.mygithubuser2.helper.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        val preferences = SettingPreferences.getInstance(dataStore)
        val settingsViewModel = obtainViewModel(this@SettingsActivity, preferences)

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity, preferences: SettingPreferences): SettingsViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, preferences)
        return ViewModelProvider(activity, factory).get(SettingsViewModel::class.java)
    }

    companion object {
        const val TITLE = "Settings"
    }
}