package com.example.ahealthychallenge.presentation.login
import android.os.Build
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.SignInActivity
import com.example.ahealthychallenge.presentation.ToastHelper
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
class SignInTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignInActivity::class.java)


    @Test
    fun createAccountBtn(){

        Espresso.onView(withId(R.id.btnCreateAccount2)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.createAccountActivity))
            .check(matches(isDisplayed()))
    }

    @Test
    fun forgetPasswordBtn(){

        Espresso.onView(withId(R.id.btnForget)).perform(click())
        Espresso.onView(withId(R.id.resetPswActivity))
            .check(matches(isDisplayed()))
    }

    @Test
    fun signInFail(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //not existing email
        Espresso.onView(withId(R.id.email_edit_text)).perform(typeText("invalid.mail@gmail.com"))
        Espresso.onView(withId(R.id.password_edit_text)).perform(typeText("Password")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnSignIn)).perform(click())
        Thread.sleep(2000)
        assertTrue(toastHelper.hasToast("sign in failed"))

        //PROVARE DA DISPOSITIVO FISICO
        //onView(withText("sign in failed")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }

    @Test
    fun signInNotVerifiedEmail(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //email and password of an user with not verified email
        Espresso.onView(withId(R.id.email_edit_text)).perform(typeText("prova@gmail.com"))
        Espresso.onView(withId(R.id.password_edit_text)).perform(typeText("ciao1234")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnSignIn)).perform(click())
        Thread.sleep(2000)
        assertTrue(toastHelper.hasToast("User isn't verified. Check your email !"))
    }


    @Test
    fun signInNewUser(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //correct email and password for a new user
        Espresso.onView(withId(R.id.email_edit_text)).perform(typeText("alessandra.fiore99@gmail.com"))
        Espresso.onView(withId(R.id.password_edit_text)).perform(typeText("validPassword")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnSignIn)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.userActivity)).check(matches(isDisplayed()))

    }


    @Test
    fun signInSuccessfull(){
        val toastHelper = TestToastHelper()
        val activity = activityRule.scenario.onActivity {
            it.toastHelper = toastHelper
        }

        //correct email and password
        Espresso.onView(withId(R.id.email_edit_text)).perform(typeText("marinacampagna27@gmail.com"))
        Espresso.onView(withId(R.id.password_edit_text)).perform(typeText("ciao1234")).perform(
            ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btnSignIn)).perform(click())
        Thread.sleep(2000)
        assertTrue(toastHelper.hasToast("signed in successfully"))

        //navigation to HomeActivity

    }

}