package com.example.healthconnectsample.presentation.component

import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.healthconnectsample.R
import com.example.healthconnectsample.data.formatTime

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