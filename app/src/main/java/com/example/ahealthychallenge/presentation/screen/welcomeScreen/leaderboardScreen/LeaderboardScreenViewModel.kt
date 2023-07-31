package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderboardScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.UserPointsSheet

class LeaderboardScreenViewModel() : ViewModel() {
    val friends: MutableState<List<Friend>> = mutableStateOf(listOf())
    val currentUserPointsSheet: MutableState<UserPointsSheet> = mutableStateOf(UserPointsSheet())

    init {
        readFriends()
        readCurrentUserPointsSheet()
    }

    private fun readFriends() {
        val mockDataFriend = listOf(
            Friend(
                firstName = "Patrick",
                lastName = "Niantcho",
                username = "Patatòche",
                pointsSheet = UserPointsSheet(
                    pointsWalking = 10,
                    pointRunning = 22,
                    pointsCycling = 20,
                    pointsWorkout = 20,
                    totalPoints = 72
                )
            ),
            Friend(
                firstName = "Alessandra",
                lastName = "Fiore",
                username = "FiorelinaDelGiardino",
                pointsSheet = UserPointsSheet(
                    pointsWalking = 5,
                    pointRunning = 5,
                    pointsCycling = 20,
                    pointsWorkout = 20,
                    totalPoints = 50
                )
            ),
            Friend(
                firstName = "Gill",
                lastName = "Hunter",
                username = "GilHunt",
                pointsSheet = UserPointsSheet(
                    pointsWalking = 20,
                    pointRunning = 4,
                    pointsCycling = 20,
                    pointsWorkout = 5,
                    totalPoints = 49
                )
            ),
            Friend(
                firstName = "Josh",
                lastName = "Salter",
                username = "SaltyGuy",
                pointsSheet = UserPointsSheet(
                    pointsWalking = 20,
                    pointRunning = 6,
                    pointsCycling = 7,
                    pointsWorkout = 7,
                    totalPoints = 40
                )
            ),
            Friend(
                firstName = "Micheal",
                lastName = "Roberts",
                username = "Miro",
                pointsSheet = UserPointsSheet(
                    pointsWalking = 3,
                    pointRunning = 1,
                    pointsCycling = 30,
                    pointsWorkout = 1,
                    totalPoints = 35
                )
            ),
            Friend(
                firstName = "Vince",
                lastName = "sky",
                username = "skusku",
                pointsSheet = UserPointsSheet(
                    pointsWalking = 1,
                    pointRunning = 10,
                    pointsCycling = 2,
                    pointsWorkout = 20,
                    totalPoints = 33
                )
            )
        )
        friends.value = mockDataFriend
    }

    private fun readCurrentUserPointsSheet() {
        val pointsSheet = UserPointsSheet(
            pointsWalking = 10,
            pointRunning = 21,
            pointsCycling = 43,
            pointsWorkout = 12
        )
        currentUserPointsSheet.value = pointsSheet
    }
}

class LeaderboardScreenViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardScreenViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}