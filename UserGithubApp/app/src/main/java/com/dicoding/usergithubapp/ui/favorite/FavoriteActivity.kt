package com.dicoding.usergithubapp.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.usergithubapp.data.Resource
import com.dicoding.usergithubapp.databinding.ActivityFavoriteBinding
import com.dicoding.usergithubapp.model.User
import com.dicoding.usergithubapp.ui.adapter.UserAdapter
import com.dicoding.usergithubapp.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(), ViewStateCallback<List<User>> {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var userAdapter: UserAdapter
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        userAdapter = UserAdapter()

        favoriteBinding.rvFavorite.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@FavoriteActivity, {
                when(it) {
                    is Resource.Error -> onFailed(it.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> it.data?.let { it1 -> onSuccess(it1) }
                }
            })
        }
    }

    override fun onSuccess(data: List<User>) {
        favoriteBinding.apply {
            favoriteProgressBar.visibility = invisible
        }
        userAdapter.setAllData(data)
    }

    override fun onLoading() {
        favoriteBinding.apply {
            favoriteProgressBar.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        if (message == null) {
            favoriteBinding.apply {
                favoriteProgressBar.visibility = invisible
                rvFavorite.visibility = invisible
                tvFavoriteError.text = "Tidak ada pengguna di daftar favorit"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@FavoriteActivity, {
                when(it) {
                    is Resource.Error -> onFailed(it.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> it.data?.let { it1 -> onSuccess(it1) }
                }
            })
        }
    }
}