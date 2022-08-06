package com.dicoding.theozu.mygithubuser2.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.theozu.mygithubuser2.api.ApiConfig
import com.dicoding.theozu.mygithubuser2.api.ItemsItem
import com.dicoding.theozu.mygithubuser2.api.UserResponse
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUserByUsername(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value =response.body()?.items
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}