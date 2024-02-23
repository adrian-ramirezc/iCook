package com.example.icook.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Snackbar(
    hostState: SnackbarHostState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        SnackbarHost(hostState = hostState)
    }
}


@Preview(showBackground = true)
@Composable
fun SnackbarPreview(){
    Snackbar(hostState = SnackbarHostState())
}