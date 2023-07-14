package com.example.ahealthychallenge.presentation.largeScreenLayout

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.navigation.DrawerItem
import com.example.ahealthychallenge.presentation.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun LargeScreenDrawer(
    scope: CoroutineScope,
    navController: NavController
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LazyColumn() {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .width(96.dp)
                        .clickable {
                            navController.navigate(Screen.WelcomeScreen.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                        },
                    painter = painterResource(id = R.drawable.ic_health_connect_logo),
                    contentDescription = stringResource(id = R.string.health_connect_logo)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.app_name)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        Screen.values().filter { it.hasMenuItem }.forEach { item ->
            item {
                DrawerItem(
                    item = item,
                    selected = item.route == currentRoute,
                    onItemClick = {
                        navController.navigate(item.route) {
                            // See: https://developer.android.com/jetpack/compose/navigation#nav-to-composable
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}