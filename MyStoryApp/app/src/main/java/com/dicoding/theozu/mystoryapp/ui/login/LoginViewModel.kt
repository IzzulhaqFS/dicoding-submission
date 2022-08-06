package com.dicoding.theozu.mystoryapp.ui.login

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.theozu.mystoryapp.api.ApiConfig
import com.dicoding.theozu.mystoryapp.api.response.LoginResponse
import com.dicoding.theozu.mystoryapp.model.UserModel
import com.dicoding.theozu.mystoryapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: UserPreference) : ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isSuccessful.value = true
                    val name = response.body()?.loginResult?.name
                    val userId = response.body()?.loginResult?.userId
                    val token = response.body()?.loginResult?.token

                    val user = UserModel(name.toString(), userId.toString(), token.toString(), true)

                    viewModelScope.launch {
                        preference.saveUser(user)
                    }
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
                else {
                    _isSuccessful.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccessful.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}