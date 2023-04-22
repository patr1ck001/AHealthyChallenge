package com.example.ahealthychallenge.presentation.component

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.ahealthychallenge.R

@Composable
fun SessionSummary (
    @StringRes labelId: Int,
    value: String
){
    Text(
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.caption,

        text = stringResource(
            R.string.summary_layout,
            value ?: "N/A",
            stringResource(labelId),
        )
    )
}