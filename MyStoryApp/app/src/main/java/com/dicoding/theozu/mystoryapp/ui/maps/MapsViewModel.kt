package com.dicoding.theozu.mystoryapp.ui.maps

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.theozu.mystoryapp.api.ApiConfig
import com.dicoding.theozu.mystoryapp.api.response.ListStoryItem
import com.dicoding.theozu.mystoryapp.api.response.StoriesResponse
import com.dicoding.theozu.mystoryapp.model.UserModel
import com.dicoding.theozu.mystoryapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val preference: UserPreference) : ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun setListStory(token: String) {
        val client = ApiConfig.getApiService().getStoriesWithLocation(token = "Bearer $token", 1)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    _listStory.value = response.body()?.listStory
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}