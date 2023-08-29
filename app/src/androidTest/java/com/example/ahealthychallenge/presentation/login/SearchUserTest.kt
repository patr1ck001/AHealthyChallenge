package com.example.ahealthychallenge.presentation.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.SearchUserActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@LargeTest
class SearchUserTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SearchUserActivity::class.java)

    @Test
    fun notExistingUsername(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        Espresso.onView(ViewMatchers.withId(R.id.search_text)).perform(ViewActions.typeText("claudia"))

        Espresso.onView(ViewMatchers.withId(R.id.search_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Assert.assertTrue(toastHelper.hasToast("username not founded !"))
    }

    @Test
    fun validUsername(){

        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        Espresso.onView(ViewMatchers.withId(R.id.search_text)).perform(ViewActions.typeText("pat"))
        Espresso.onView(ViewMatchers.withId(R.id.search_btn)).perform(ViewActions.click())
        Thread.sleep(2000)

        //the user with the specified username is shown
        Espresso.onView(ViewMatchers.withId(R.id.name_surname))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun sendAnDeleteRequest(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper;
            it.currentUsername= "al_fio"
        }

        Espresso.onView(ViewMatchers.withId(R.id.search_text)).perform(ViewActions.typeText("pat"))
        Espresso.onView(ViewMatchers.withId(R.id.search_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).check(ViewAssertions.matches(withText("Request Sent")))
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).check(ViewAssertions.matches(withText("Send Request")))
    }

    @Test
    fun acceptAndDeleteFriend(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper;
            it.currentUsername= "al_fio"
        }

        Espresso.onView(ViewMatchers.withId(R.id.search_text)).perform(ViewActions.typeText("pat"))
        Espresso.onView(ViewMatchers.withId(R.id.search_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).check(ViewAssertions.matches(withText("Delete")))
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        //verify if dialog is shown
        Espresso.onView(withText("Delete friend. Are you sure?")).check(matches(isDisplayed()))
        Espresso.onView(withText("Yes")).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).check(ViewAssertions.matches(withText("Send Request")))

    }

    @Test
    fun refuseRequest(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper;
            it.currentUsername= "al_fio"
        }

        Espresso.onView(ViewMatchers.withId(R.id.search_text)).perform(ViewActions.typeText("pat"))
        Espresso.onView(ViewMatchers.withId(R.id.search_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.refuse_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.request_btn)).check(ViewAssertions.matches(withText("Send Request")))
    }
}