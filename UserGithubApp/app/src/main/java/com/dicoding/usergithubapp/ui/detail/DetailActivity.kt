package com.dicoding.usergithubapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.usergithubapp.R
import com.dicoding.usergithubapp.util.Constanta.TAB_TITLES
import com.dicoding.usergithubapp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.dicoding.usergithubapp.util.Constanta.EXTRA_USER
import com.dicoding.usergithubapp.data.Resource
import com.dicoding.usergithubapp.model.User
import com.dicoding.usergithubapp.util.ViewStateCallback
import com.dicoding.usergithubapp.detail.DetailViewModel
import com.dicoding.usergithubapp.ui.adapter.FollowPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), ViewStateCallback<User?> {

    private lateinit var detailBinding: ActivityDetailBinding
    private val viewModel  by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        val username = intent.getStringExtra(EXTRA_USER)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDetailUser(username.toString()).observe(this@DetailActivity, {
                when (it) {
                    is Resource.Error -> onFailed(it.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> onSuccess(it.data)
                }
            })
        }

        val pageAdapter = FollowPagerAdapter(this, username.toString())

        detailBinding.apply {
            viewPager.adapter = pageAdapter
            TabLayoutMediator(tabs, viewPager) { tabs, position ->
                tabs.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSuccess(data: User?) {

        detailBinding.apply {
            tvDetailNumberOfRepos.text = data?.repository.toString()
            tvDetailNumberOfFollowers.text = data?.follower.toString()
            tvDetailNumberOfFollowing.text = data?.following.toString()
            tvDetailName.text = data?.name
            tvDetailLocation.text = data?.location

            Glide.with(this@DetailActivity)
                .load(data?.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(ivDetailAvatar)

            ivFavorite.visibility = View.VISIBLE

            if (data?.isFavorite == true)
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_black))
            else
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_unfavorite_24))

            supportActionBar?.title = data?.username
            detailProgressBar.visibility = invisible

            if (data?.location == null)
                tvDetailLocation.text = resources.getString(R.string.location)


            ivFavorite.setOnClickListener {
                if (data?.isFavorite == true) {
                    data.isFavorite = false
                    viewModel.deleteFavoriteUser(data)
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_unfavorite_24))
                } else {
                    data?.isFavorite = true
                    data?.let { it1 -> viewModel.insertFavoriteUser(it1) }
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_black))
                }
            }
        }
    }

    override fun onLoading() {
        detailBinding.ivFavorite.visibility = View.INVISIBLE
    }

    override fun onFailed(message: String?) {
        detailBinding.ivFavorite.visibility = View.INVISIBLE
    }
}