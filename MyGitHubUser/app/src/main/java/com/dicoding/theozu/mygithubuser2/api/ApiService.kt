package com.dicoding.theozu.mygithubuser2.api

import com.dicoding.theozu.mygithubuser2.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getListUserByUsername(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getDetailUserByUsername(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowersByUsername(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowingByUsername(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}