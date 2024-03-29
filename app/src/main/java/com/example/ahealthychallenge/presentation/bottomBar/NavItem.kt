package com.example.ahealthychallenge.presentation.bottomBar
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)
