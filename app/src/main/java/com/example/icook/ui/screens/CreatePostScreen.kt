package com.example.icook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.icook.R
import com.example.icook.ui.components.BottomNavigationBar

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    onProfileButtonClicked: () -> Unit = {},
    onHomeButtonClicked: () -> Unit = {},
    ) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onHomeButtonClicked
            ) {
                Icon(Icons.Outlined.Clear, contentDescription = null)
            }
            Text(
                text = "New Post",
                modifier = Modifier.align(Alignment.CenterVertically)
                )
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Post")
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ceviche),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Row {
                    Text(text = "Recents")
                    Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                }
            }
            TextButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Outlined.Add, contentDescription = "Select another pic from gallery")
            }
        }
        Text(
            text = "Description",
            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
        )
        OutlinedTextField(
            value = "Add a description to your post",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Choose any other picture from your gallery:",
            modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 5.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier.weight(1f)
        ){
            items(18) {Image(
                painter = painterResource(id = R.drawable.ceviche),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )}
        }
        BottomNavigationBar(
            onHomeButtonClicked = onHomeButtonClicked,
            onProfileButtonClicked = onProfileButtonClicked,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostPreview(){
    CreatePostScreen()
}