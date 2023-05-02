package com.dicoding.usergithubapp.ui.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.usergithubapp.*
import com.dicoding.usergithubapp.data.Resource
import com.dicoding.usergithubapp.ui.adapter.UserAdapter
import com.dicoding.usergithubapp.databinding.FragmentFollowerBinding
import com.dicoding.usergithubapp.model.User
import com.dicoding.usergithubapp.util.ViewStateCallback

class FollowerFragment: Fragment(), ViewStateCallback<List<User>> {

    companion object {
        private const val KEY_BUNDLE = "USERNAME"

        fun getInstance(username: String): Fragment {
            return FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE, username)
                }
            }
        }
    }

    private val followerBinding: FragmentFollowerBinding by viewBinding()
    private lateinit var viewModel: FollowerViewModel
    private lateinit var userAdapter: UserAdapter
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(KEY_BUNDLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(FollowerViewModel::class.java)

        userAdapter = UserAdapter()
        followerBinding.rvListUserFollower.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.getUserFollowers(username.toString()).observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> onFailed(it.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> it.data?.let { it1 -> onSuccess(it1) }
            }
        })

    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllData(data)
        followerBinding.apply {
            tvMessage.visibility = invisible
            followProgressBar.visibility = invisible
            rvListUserFollower.visibility = visible
        }
    }

    override fun onLoading() {
        followerBinding.apply {
            tvMessage.visibility = invisible
            followProgressBar.visibility = visible
            rvListUserFollower.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        followerBinding.apply {
            if (message == null) {
                tvMessage.text = resources.getString(R.string.followers_not_found)
                tvMessage.visibility = visible
            } else {
                tvMessage.text = message
                tvMessage.visibility = visible
            }
            followProgressBar.visibility = invisible
            rvListUserFollower.visibility = invisible
        }
    }
}