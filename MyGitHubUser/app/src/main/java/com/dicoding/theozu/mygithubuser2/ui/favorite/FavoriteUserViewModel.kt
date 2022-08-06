package com.dicoding.theozu.mygithubuser2.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.theozu.mygithubuser2.database.FavoriteUser
import com.dicoding.theozu.mygithubuser2.repository.FavoriteUserRepository
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingPreferences

class FavoriteUserViewModel(application: Application, private val preferences: SettingPreferences) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUsers()

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }
}