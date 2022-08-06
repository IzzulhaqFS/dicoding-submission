package com.dicoding.theozu.mygithubuser2.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.theozu.mygithubuser2.api.ApiConfig
import com.dicoding.theozu.mygithubuser2.api.DetailUserResponse
import com.dicoding.theozu.mygithubuser2.database.FavoriteUser
import com.dicoding.theozu.mygithubuser2.repository.FavoriteUserRepository
import com.dicoding.theozu.mygithubuser2.ui.settings.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application, private val preferences: SettingPreferences) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUserByUsername(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun insert(id: Int, username: String, avatar: String) {
        val favoriteUser = FavoriteUser(id, username, avatar)
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(id: Int) {
        mFavoriteUserRepository.delete(id)
    }

    suspend fun checkFavoriteUser(id: Int) = mFavoriteUserRepository.checkFavoriteUser(id)

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    companion object {
        const val TAG = "DetailUserViewModel"
    }
}