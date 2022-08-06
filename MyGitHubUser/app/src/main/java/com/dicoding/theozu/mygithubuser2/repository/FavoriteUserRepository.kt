package com.dicoding.theozu.mygithubuser2.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.theozu.mygithubuser2.database.FavoriteUser
import com.dicoding.theozu.mygithubuser2.database.FavoriteUserDao
import com.dicoding.theozu.mygithubuser2.database.FavoriteUserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUsers()

    fun insert(favoriteUser: FavoriteUser) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteUserDao.insert(favoriteUser)
        }
    }

    fun delete(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteUserDao.delete(id)
        }
    }

    suspend fun checkFavoriteUser(id: Int) = mFavoriteUserDao.checkFavoriteUser(id)
}