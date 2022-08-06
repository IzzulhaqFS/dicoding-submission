package com.dicoding.theozu.mygithubuser2.ui.detailuser.follower

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.theozu.mygithubuser2.api.ApiConfig
import com.dicoding.theozu.mygithubuser2.api.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowersByUsername(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
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