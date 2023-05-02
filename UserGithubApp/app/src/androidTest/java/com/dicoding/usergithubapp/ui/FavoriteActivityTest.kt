package com.dicoding.usergithubapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.dicoding.usergithubapp.R
import com.dicoding.usergithubapp.ui.adapter.UserAdapter
import com.dicoding.usergithubapp.ui.favorite.FavoriteActivity
import com.dicoding.usergithubapp.util.EspressoIdlingResource
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteActivityTest {

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        ActivityScenario.launch(FavoriteActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun testDeleteAllFavoriteUser() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_favorite))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        while (true) {
            try {
                Espresso.onView(ViewMatchers.withId(R.id.rv_favorite)).perform(
                    actionOnItemAtPosition<UserAdapter.UserViewHolder>(0, ViewActions.click())
                )

                Espresso.onView(ViewMatchers.withId(R.id.user_detail_container))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                Espresso.onView(ViewMatchers.withId(R.id.tabs))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                Espresso.onView(ViewMatchers.withId(R.id.iv_favorite))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

                Espresso.onView(ViewMatchers.withId(R.id.iv_favorite)).perform(ViewActions.click())
                Espresso.pressBack()
            } catch (e: Exception) {
                break
            }
        }

        Espresso.onView(ViewMatchers.withId(R.id.tv_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}