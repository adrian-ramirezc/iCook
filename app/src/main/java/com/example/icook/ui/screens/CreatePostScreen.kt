package com.example.icook.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.icook.R
import com.example.icook.ui.components.BottomNavigationBar
import coil.compose.rememberImagePainter

@Composable
fun CreatePostScreen(
    onProfileButtonClicked: () -> Unit = {},
    onHomeButtonClicked: () -> Unit = {},
    ) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 15.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ){
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
                painter = selectedImageUri?.let {uri ->rememberImagePainter(uri)} ?: run {painterResource(id = R.drawable.default_post)},
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height((0.5 * LocalConfiguration.current.screenHeightDp).dp)
            )
            TextField(
                modifier = Modifier
                    .padding(15.dp),
                value = "Add a description to your post",
                onValueChange = {},
                maxLines = 1,
            )
        }
        //Spacer(modifier = Modifier.size(128.dp))
        Button(
            onClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {
            Text(text = "Open Gallery")
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