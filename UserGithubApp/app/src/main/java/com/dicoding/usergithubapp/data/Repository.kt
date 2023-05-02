package com.dicoding.usergithubapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.usergithubapp.model.SearchResponse
import com.dicoding.usergithubapp.data.datastore.UserDataStore
import com.dicoding.usergithubapp.data.local.UserDao
import com.dicoding.usergithubapp.data.local.UserDatabase
import com.dicoding.usergithubapp.data.remote.ApiService
import com.dicoding.usergithubapp.data.remote.RetrofitService
import com.dicoding.usergithubapp.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Application

class Repository (private val application: Application) {
    private val retrofit: ApiService
    private val dao: UserDao
    private val dataStore: UserDataStore

    init {
        retrofit = RetrofitService.create()
        val database: UserDatabase = UserDatabase.getInstance(application)
        dao = database.userDao()
        dataStore = UserDataStore.getInstance(application)
    }

    fun searchUser(query: String): LiveData<Resource<List<User>>> {
        val listUser = MutableLiveData<Resource<List<User>>>()

        listUser.postValue(Resource.Loading())
        retrofit.searchUsers(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val list = response.body()?.items
                if (list.isNullOrEmpty())
                    listUser.postValue(Resource.Error(null))
                else
                    listUser.postValue(Resource.Success(list))
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })
        return listUser
    }

    suspend fun getDetailUser(username: String): LiveData<Resource<User>> {
        val user = MutableLiveData<Resource<User>>()

        if (dao.getFavoriteDetailUser(username) != null) {
            user.postValue(Resource.Success(dao.getFavoriteDetailUser(username)))
        } else {
            retrofit.getDetailUser(username).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val result = response.body()
                    user.postValue(Resource.Success(result))
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                }
            })
        }
        return user
    }

    fun getUserFollowing(username: String): LiveData<Resource<List<User>>> {

        val listUser = MutableLiveData<Resource<List<User>>>()

        listUser.postValue(Resource.Loading())
        retrofit.getUserFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty())
                    listUser.postValue(Resource.Error(null))
                else
                    listUser.postValue(Resource.Success(list))
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })
        return listUser
    }

    fun getUserFollowers(username: String): LiveData<Resource<List<User>>> {

        val listUser = MutableLiveData<Resource<List<User>>>()

        listUser.postValue(Resource.Loading())
        retrofit.getUserFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty())
                    listUser.postValue(Resource.Error(null))
                else
                    listUser.postValue(Resource.Success(list))
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })

        return listUser
    }

    suspend fun getFavoriteList(): LiveData<Resource<List<User>>> {

        val listFavorite = MutableLiveData<Resource<List<User>>>()
        listFavorite.postValue(Resource.Loading())

        if (dao.getFavoriteListUser().isNullOrEmpty())
            listFavorite.postValue(Resource.Error(null))
        else
            listFavorite.postValue(Resource.Success(dao.getFavoriteListUser()))

        return listFavorite
    }

    suspend fun insertFavoriteUser(user: User) = dao.insertFavoriteUser(user)

    suspend fun deleteFavoriteUser(user: User) = dao.deleteFavoriteUser(user)

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = dataStore.saveThemeSetting(isDarkModeActive)

    fun getThemeSetting() = dataStore.getThemeSetting()
}