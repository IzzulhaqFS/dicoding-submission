package com.dicoding.theozu.mystoryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.theozu.mystoryapp.api.ApiConfig
import com.dicoding.theozu.mystoryapp.model.UserPreference
import com.dicoding.theozu.mystoryapp.ui.addstory.AddStoryViewModel
import com.dicoding.theozu.mystoryapp.ui.login.LoginViewModel
import com.dicoding.theozu.mystoryapp.ui.main.MainViewModel
import com.dicoding.theozu.mystoryapp.ui.maps.MapsViewModel
import com.dicoding.theozu.mystoryapp.ui.register.RegisterViewModel

class ViewModelFactory(private val preference: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(preference, ApiConfig.getApiService()) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preference) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel() as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(preference) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(preference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }
}