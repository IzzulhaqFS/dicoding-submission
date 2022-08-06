package com.dicoding.theozu.mygithubuser2.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.theozu.mygithubuser2.ui.detailuser.DetailUserViewModel
import com.dicoding.theozu.mygithubuser2.ui.favorite.FavoriteUserViewModel
import com.dicoding.theozu.mygithubuser2.ui.main.MainViewModel
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingPreferences
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingsViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(private val mApplication: Application, private val mPreferences: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, preferences: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, preferences)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailUserViewModel::class.java) -> {
                DetailUserViewModel(mApplication, mPreferences) as T
            }
            modelClass.isAssignableFrom(FavoriteUserViewModel::class.java) -> {
                FavoriteUserViewModel(mApplication, mPreferences) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(mPreferences) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(mPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}