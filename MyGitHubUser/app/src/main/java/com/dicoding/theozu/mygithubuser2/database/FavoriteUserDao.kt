package com.dicoding.theozu.mygithubuser2.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteUser: FavoriteUser)

    @Query("DELETE FROM FavoriteUser WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM FavoriteUser ORDER BY id ASC")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT COUNT(*) FROM FavoriteUser WHERE id = :id")
    suspend fun checkFavoriteUser(id: Int): Int
}