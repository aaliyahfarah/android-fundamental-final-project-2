package com.dicoding.usergithubapp.util

import androidx.annotation.StringRes
import com.dicoding.usergithubapp.R

object Constanta {
    const val EXTRA_USER = "EXTRA_USER"

    @StringRes
    val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )
    const val BASE_URL = "https://api.github.com/"
    const val TIME_SPLASH = 2000L
}