package com.example.ahealthychallenge.presentation.screen.welcomeScreen.friendsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.User
import com.example.ahealthychallenge.presentation.component.getIconId
import com.example.ahealthychallenge.presentation.theme.HealthConnectBlue

@Composable
fun FriendsScreen(
    onDetailsClick: (String) -> Unit = {},
    onAddFriend: () -> Unit = {}
) {

    val users = listOf(
        User(
            firstName = "Patrick",
            lastName = "Niantcho",
            username = "Patatòche"
        ),
        User(
            firstName = "Alessandra",
            lastName = "Fiore",
            username = "FiorelinaDelGiardino"
        ),
        User(
            firstName = "Gill",
            lastName = "Hunter",
            username = "GilHunt"
        ),
        User(
            firstName = "Josh",
            lastName = "Salter",
            username = "SaltyGuy"
        ),
        User(
            firstName = "Micheal",
            lastName = "Roberts",
            username = "Miro"
        ),
        User(
            firstName = "Vince",
            lastName = "sky",
            username = "skusku"
        ),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        users.forEach { user ->
            item {
                Card() {
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_friends),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(1f)
                        )
                        Column(
                            modifier = Modifier.weight(4f)
                        ) {
                            Row(horizontalArrangement = Arrangement.Center) {
                                user.username?.let { username ->
                                    Text(
                                        text = username,
                                        textAlign = TextAlign.Center,
                                        color = HealthConnectBlue,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                user.firstName?.let {
                                    Text(
                                        text = it,
                                        modifier = Modifier
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                user.lastName?.let {
                                    Text(
                                        text = it,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(4.dp),
                onClick = {
                    onAddFriend()
                }) {
                Text(stringResource(id = R.string.add_friend))
            }
        }
    }
}

