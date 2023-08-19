package com.example.ahealthychallenge.presentation.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.ResetPasswordActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@LargeTest
class ResetPasswordTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ResetPasswordActivity::class.java)

    @Test
    fun resetPassword(){

        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        Espresso.onView(ViewMatchers.withId(R.id.email_text)).perform(ViewActions.typeText("alessandra.fiore99@gmail.com"))
            .perform(ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.btnReset)).perform(ViewActions.click())
        Thread.sleep(2000)
        Assert.assertTrue(toastHelper.hasToast("New password sent !"))

    }

}