package com.movies.sample.features.movies

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.tabs.TabLayout
import com.movies.sample.R
import com.movies.sample.features.movies.moviesList.MoviesAdapter
import com.movies.sample.features.movies.util.EspressoIdlingResource
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MoviesActivityTest {

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun makeMovieFavorite() {
        val activityScenario = ActivityScenario.launch(MoviesActivity::class.java)

        // check is main screen visible
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))

        // open movie
        onView(allOf(isDisplayed(), withId(R.id.movieList)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MoviesAdapter.ViewHolder>(0, click()))

        // check is detail screen visible
        onView(withId(R.id.movieDetails)).check(matches(isDisplayed()))

        // click favorites
        onView(withId(R.id.fav_iv)).perform(click())

        // move back
        pressBack()

        // check is main screen visible
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun openFavoritesMovies() {
        val activityScenario = ActivityScenario.launch(MoviesActivity::class.java)

        // check is main screen visible
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))

        // open fav movies
        onView(withId(R.id.tabs)).perform(selectTabAtPosition(1))

        // check is favorites active
        onView(withText(R.string.fav_movies_screen_title)).check(matches(withTextColor(R.color.colorTextSecondary)))

        // open movies
        onView(withId(R.id.tabs)).perform(selectTabAtPosition(0))

        activityScenario.close()
    }


}

fun selectTabAtPosition(tabIndex: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription() = "with tab at index $tabIndex"

        override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

        override fun perform(uiController: UiController, view: View) {
            val tabLayout = view as TabLayout
            val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                ?: throw PerformException.Builder()
                    .withCause(Throwable("No tab at index $tabIndex"))
                    .build()

            tabAtIndex.select()
        }
    }
}

fun withTextColor(@ColorRes colorRes: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View): Boolean {
            return if (view !is TextView) false else view.currentTextColor == ContextCompat.getColor(view.getContext(), colorRes)

        }

        override fun describeTo(description: Description) {
            description.appendText("Expected Color Resource Id => " + colorRes)
        }
    }
}