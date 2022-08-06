package com.dicoding.theozu.mystoryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.theozu.mystoryapp.api.ApiService
import com.dicoding.theozu.mystoryapp.api.response.ListStoryItem
import com.dicoding.theozu.mystoryapp.model.UserPreference

class StoryPagingSource(private val apiService: ApiService, private val token: String) :
    PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStories(
                token = "Bearer $token",
                position,
                params.loadSize
            )
            val stories = responseData.listStory

            LoadResult.Page(
                data =  stories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (stories.isNullOrEmpty()) null else position + 1
            )
        }
        catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}