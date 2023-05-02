package com.dicoding.usergithubapp.model

import androidx.room.Entity
import com.squareup.moshi.Json
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (

    @field:Json(name = "id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int? = 0,

    @field:Json(name = "login")
    @ColumnInfo(name = "username")
    val username: String? = "",

    @field:Json(name = "name")
    @ColumnInfo(name = "name")
    val name: String? = "",

    @field:Json(name = "location")
    @ColumnInfo(name = "location")
    val location: String? = "",

    @field:Json(name = "public_repos")
    @ColumnInfo(name = "repository")
    val repository: Int? = 0,

    @field:Json(name = "followers")
    @ColumnInfo(name = "follower")
    val follower: Int? = 0,

    @field:Json(name = "following")
    @ColumnInfo(name = "following")
    val following: Int? = 0,

    @field:Json(name = "avatar_url")
    @ColumnInfo(name = "avatar")
    val avatar: String? = "",

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = false
)