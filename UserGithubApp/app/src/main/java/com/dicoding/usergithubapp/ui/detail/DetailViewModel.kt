package com.dicoding.usergithubapp.detail

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.usergithubapp.data.Repository
import com.dicoding.usergithubapp.model.User
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): AndroidViewModel(application) {

    val repository = Repository(application)

    suspend fun getDetailUser(username: String) = repository.getDetailUser(username)

    fun insertFavoriteUser(user: User) = viewModelScope.launch {
        repository.insertFavoriteUser(user)
    }

    fun deleteFavoriteUser(user: User) = viewModelScope.launch {
        repository.deleteFavoriteUser(user)
    }
}