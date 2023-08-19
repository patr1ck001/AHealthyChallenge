package com.example.ahealthychallenge.presentation.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.CreateAccountActivity
import com.example.ahealthychallenge.presentation.UserActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@LargeTest
class UserTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(UserActivity::class.java)

    @Test
    fun existingUsername(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        Espresso.onView(ViewMatchers.withId(R.id.first_name)).perform(ViewActions.typeText("Alessandra"))
        Espresso.onView(ViewMatchers.withId(R.id.last_name)).perform(ViewActions.typeText("Fiore"))
        Espresso.onView(ViewMatchers.withId(R.id.user_name)).perform(ViewActions.typeText("pat"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.save_button)).perform(ViewActions.click())
        Thread.sleep(2000)
        Assert.assertTrue(toastHelper.hasToast("Username already exists !"))
    }

    @Test
    fun emptyField(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        Espresso.onView(ViewMatchers.withId(R.id.last_name)).perform(ViewActions.typeText("Fiore"))
        Espresso.onView(ViewMatchers.withId(R.id.user_name)).perform(ViewActions.typeText("pat"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.save_button)).perform(ViewActions.click())
        Thread.sleep(2000)
        Assert.assertTrue(toastHelper.hasToast("Fill all fields"))

    }

    @Test
    fun correctProfile(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        Espresso.onView(ViewMatchers.withId(R.id.first_name)).perform(ViewActions.typeText("Alessandra"))
        Espresso.onView(ViewMatchers.withId(R.id.last_name)).perform(ViewActions.typeText("Fiore"))
        Espresso.onView(ViewMatchers.withId(R.id.user_name)).perform(ViewActions.typeText("al_fio"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.save_button)).perform(ViewActions.click())
        Thread.sleep(4000)
        Assert.assertTrue(toastHelper.hasToast("Profile successfully updated !"))

        //start of home activity

    }
}