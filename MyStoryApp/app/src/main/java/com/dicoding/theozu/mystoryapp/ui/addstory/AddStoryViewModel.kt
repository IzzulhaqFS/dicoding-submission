package com.dicoding.theozu.mystoryapp.ui.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.theozu.mystoryapp.api.ApiConfig
import com.dicoding.theozu.mystoryapp.api.response.UploadImageResponse
import com.dicoding.theozu.mystoryapp.model.UserModel
import com.dicoding.theozu.mystoryapp.model.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val preference: UserPreference) : ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun uploadStory(token: String, photo: MultipartBody.Part, description: RequestBody) {
        val client = ApiConfig.getApiService().addStory(token = "Bearer $token", photo, description)
        client.enqueue(object : Callback<UploadImageResponse> {
            override fun onResponse(
                call: Call<UploadImageResponse>,
                response: Response<UploadImageResponse>
            ) {
                if (response.isSuccessful) {
                    _isSuccessful.value = true
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
                else {
                    _isSuccessful.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                _isSuccessful.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}