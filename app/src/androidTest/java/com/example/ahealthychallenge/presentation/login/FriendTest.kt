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
import com.example.ahealthychallenge.presentation.FriendsActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

@LargeTest
class FriendTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(FriendsActivity::class.java)


    @Test
    fun searchUsers(){
        Espresso.onView(ViewMatchers.withId(R.id.add_friend_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.searchUserActivity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun acceptFriend(){

        val activity = activityRule.scenario.onActivity {
            it.username= "mari";
            it.user = "Z0SGGGSX7dPgJHQDeurjU6UwtHE3"
        }

        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.accept_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.accept_btn)).check(matches(withText("Friend")))


    }

    @Test
    fun refuseFriend(){
        val activity = activityRule.scenario.onActivity {
            it.username= "mari";
            it.user = "Z0SGGGSX7dPgJHQDeurjU6UwtHE3"
        }

        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.refuse_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.refuse_btn)).check(matches(not(isDisplayed())))
    }

    @Test
    fun deleteFriend(){
        val activity = activityRule.scenario.onActivity {
            it.username= "mari";
            it.user = "Z0SGGGSX7dPgJHQDeurjU6UwtHE3"
        }

        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(R.id.delete_btn)).perform(ViewActions.click())
        Thread.sleep(2000)
        //verify if dialog is shown
        Espresso.onView(withText("Delete friend. Are you sure?")).check(matches(isDisplayed()))
        Espresso.onView(withText("Yes")).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.delete_btn)).check(matches(not(isDisplayed())))

    }
}