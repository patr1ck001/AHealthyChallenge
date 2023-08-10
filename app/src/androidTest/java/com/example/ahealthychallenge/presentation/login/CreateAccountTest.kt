package com.example.ahealthychallenge.presentation.login


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.ahealthychallenge.presentation.CreateAccountActivity
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.SignInActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
class CreateAccountTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CreateAccountActivity::class.java)

    @Test
    fun createAccountSuccess(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //not registered valid email
        Espresso.onView(withId(R.id.email_edit_text2)).perform(typeText("silvialauriola1999@icloud.com"))
        Espresso.onView(withId(R.id.password_edit_text2)).perform(typeText("ciao1234"))
        Espresso.onView(withId(R.id.confirm_password_edit_text)).perform(typeText("ciao1234")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnCreateAccount)).perform(click())
        Thread.sleep(4000)
        Assert.assertTrue(toastHelper.hasToast("Check your email for verification !"))
        onView(withId(R.id.signInActivity)).check(matches(isDisplayed()))

    }

    @Test
    fun createAccountFail(){

        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //already registered email
        Espresso.onView(withId(R.id.email_edit_text2)).perform(typeText("marinacampagna27@gmail.com"))
        Espresso.onView(withId(R.id.password_edit_text2)).perform(typeText("ciao1234"))
        Espresso.onView(withId(R.id.confirm_password_edit_text)).perform(typeText("ciao1234")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnCreateAccount)).perform(click())
        Thread.sleep(2000)
        Assert.assertTrue(toastHelper.hasToast("failed to Authenticate !"))

    }

    @Test
    fun createAccountPasswordNotMatching(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //different passwords
        Espresso.onView(withId(R.id.email_edit_text2)).perform(typeText("marinacampagna27@gmail.com"))
        Espresso.onView(withId(R.id.password_edit_text2)).perform(typeText("ciao1234"))
        Espresso.onView(withId(R.id.confirm_password_edit_text)).perform(typeText("hello1234")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnCreateAccount)).perform(click())
        Thread.sleep(2000)
        Assert.assertTrue(toastHelper.hasToast("passwords are not matching !"))
    }

    @Test
    fun createAccountPasswordShort(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //short password
        Espresso.onView(withId(R.id.email_edit_text2)).perform(typeText("marinacampagna27@gmail.com"))
        Espresso.onView(withId(R.id.password_edit_text2)).perform(typeText("ciao"))
        Espresso.onView(withId(R.id.confirm_password_edit_text)).perform(typeText("ciao")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnCreateAccount)).perform(click())
        Thread.sleep(2000)
        Assert.assertTrue(toastHelper.hasToast("password is too short !"))
    }
}