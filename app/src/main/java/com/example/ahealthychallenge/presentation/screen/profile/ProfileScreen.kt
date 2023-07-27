package com.example.ahealthychallenge.presentation.screen.profile

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.presentation.SignInActivity
import com.example.ahealthychallenge.presentation.component.CircularProgressBar
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen.CompactHomeScreen
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen.ExpendedAndMediumHomeScreen
import com.example.ahealthychallenge.presentation.theme.HealthConnectBlue
import com.example.ahealthychallenge.presentation.theme.HealthConnectGreen
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils
import com.example.ahealthychallenge.presentation.utils.NavigationType

@Composable
fun ProfileScreen(
    navigationType: NavigationType,
    currentUser: Friend,
    profileLoading: Boolean
) {
    if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
        CompactProfileScreen(
            currentUser = currentUser,
            profileLoading = profileLoading
        )
    } else {
        ExpendedAndMediumProfileScreen(
            currentUser = currentUser,
            profileLoading = profileLoading
        )
    }
}


@Composable
fun CompactProfileScreen(
    currentUser: Friend,
    profileLoading: Boolean
) {
    val context = LocalContext.current

    if (!profileLoading) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (currentUser.bitmap != null) {
                        currentUser.bitmap?.let { bm ->
                            Image(
                                bitmap = bm.asImageBitmap(),
                                contentDescription = "userImage",
                                modifier = Modifier
                                    .size(150.dp) // TODO: ADAPTABLE LAYOUT
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile_circle),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(150.dp)
                                .padding(8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${currentUser.username}",
                    color = MaterialTheme.colors.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(modifier = Modifier.weight(2f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_name_ic),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "name",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${currentUser.firstName} ${currentUser.lastName}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(30.dp))

                // number of friends row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_friends),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Total friends",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "3", // TODO: update hardcoded number
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))


                // points row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star_svgrepo_com),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Points this month",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "100", // TODO: update hardcoded number
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                // Position row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.position_ic),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Position",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "1", // TODO: update hardcoded number
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            FirebaseUtils.firebaseAuth.signOut()
                            val intent = Intent(context, SignInActivity::class.java)
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = stringResource(R.string.sign_out))
                    }
                }
            }

        }

    } else {
        CircularProgressBar(
            isDisplayed = true, Modifier.size(60.dp)
        )
    }
}

@Composable
fun ExpendedAndMediumProfileScreen(currentUser: Friend, profileLoading: Boolean) {
    val context = LocalContext.current

    if (!profileLoading) {
        Row(
            Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (currentUser.bitmap != null) {
                    currentUser.bitmap?.let { bm ->
                        Image(
                            bitmap = bm.asImageBitmap(),
                            contentDescription = "userImage",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile_circle),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${currentUser.username}",
                    color = HealthConnectBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            //Spacer(modifier = Modifier.height(25.dp))

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_name_ic),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "name",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${currentUser.firstName} ${currentUser.lastName}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                }
                //Spacer(modifier = Modifier.height(25.dp))

                // number of friends row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_friends),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Total friends",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "3", // TODO: update hardcoded number
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                //Spacer(modifier = Modifier.height(25.dp))


                // points row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star_svgrepo_com),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Points this month",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "100", // TODO: update hardcoded number
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                //Spacer(modifier = Modifier.height(25.dp))
                // Position row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.position_ic),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Position",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "1", // TODO: update hardcoded number
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            FirebaseUtils.firebaseAuth.signOut()
                            val intent = Intent(context, SignInActivity::class.java)
                            context.startActivity(intent)
                        }
                    ) {
                        Text(text = stringResource(R.string.sign_out))
                    }
                }
            }

        }

    } else {
        CircularProgressBar(
            isDisplayed = true, Modifier.size(60.dp)
        )
    }
}