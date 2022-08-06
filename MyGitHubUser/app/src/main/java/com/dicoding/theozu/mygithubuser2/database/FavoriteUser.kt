package com.dicoding.theozu.mygithubuser2.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "login")
    var login: String,

    @ColumnInfo(name = "avatar_url")
    var avatar_url: String
) : Parcelable