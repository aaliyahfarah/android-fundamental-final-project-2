package com.dicoding.usergithubapp.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.usergithubapp.data.Repository
import com.dicoding.usergithubapp.ui.setting.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = Repository(application)

    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        repository.saveThemeSetting(isDarkModeActive)
    }

    fun getThemeSetting() = repository.getThemeSetting().asLiveData(Dispatchers.IO)

}