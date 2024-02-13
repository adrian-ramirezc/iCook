package com.example.icook.ui.components

import android.view.View.OnCreateContextMenuListener
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    modifier : Modifier = Modifier.height(50.dp),
    onHomeButtonClicked: () -> Unit = {},
    onProfileButtonClicked: () -> Unit = {},
    onCreatePostButtonClicked: () -> Unit = {},
    ) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.Transparent
    ) {
        NavigationBarItem(selected = false,
            onClick = onHomeButtonClicked,
            icon = {
                Icon(Icons.Outlined.Home, contentDescription = null)
            }
        )
        NavigationBarItem(selected = false,
            onClick = onCreatePostButtonClicked,
            icon = {
                Icon(Icons.Outlined.AddCircle, contentDescription = null)
            }
        )
        NavigationBarItem(selected = false,
            onClick = onProfileButtonClicked,
            icon = {
                Icon(Icons.Outlined.AccountCircle, contentDescription = null)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview(){
    BottomNavigationBar()
}