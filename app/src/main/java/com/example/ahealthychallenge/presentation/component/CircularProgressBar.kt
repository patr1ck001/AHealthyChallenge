package com.example.ahealthychallenge.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar (
    isDisplayed: Boolean,
    modifier: Modifier
) {
    if (isDisplayed) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = modifier
            )
        }
    }
}