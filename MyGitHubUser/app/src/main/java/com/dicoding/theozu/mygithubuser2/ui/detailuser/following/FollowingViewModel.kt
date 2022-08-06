package com.dicoding.theozu.mygithubuser2.ui.detailuser.following

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.theozu.mygithubuser2.api.ApiConfig
import com.dicoding.theozu.mygithubuser2.api.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowingByUsername(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        const val TAG = "FollowersViewModel"
    }
}