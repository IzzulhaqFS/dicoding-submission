package com.dicoding.theozu.mystoryapp.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.theozu.mystoryapp.api.ApiService
import com.dicoding.theozu.mystoryapp.api.response.ListStoryItem
import com.dicoding.theozu.mystoryapp.data.StoryRepository
import com.dicoding.theozu.mystoryapp.model.UserModel
import com.dicoding.theozu.mystoryapp.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val preference: UserPreference, apiService: ApiService) : ViewModel() {
    private val storyRepository = StoryRepository(apiService)
    private lateinit var listStory: LiveData<PagingData<ListStoryItem>>

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun setListStories(token: String) {
        listStory = storyRepository.getAllStories(token).cachedIn(viewModelScope)
    }

    fun getListStories(): LiveData<PagingData<ListStoryItem>> {
        return listStory
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}