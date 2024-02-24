package com.example.icook.ui.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.icook.R
import com.example.icook.utils.loadImageFromBase64

@Composable
fun UpdateUserPicture(
    picture: String = "",
    onCloseButtonClicked: () -> Unit = {},
    onUpdateButtonClicked: (Uri?) -> Unit = {_ ->},
) {
    var selectedPictureUri by remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedPictureUri  = uri
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 15.dp
            ),
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    onClick = { onCloseButtonClicked() }
                ) {
                    Icon(Icons.Outlined.Clear, contentDescription = null)
                }
                Text(
                    text = "New Picture",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                TextButton(
                    onClick = { onUpdateButtonClicked(selectedPictureUri) }
                ) {
                    Text(text = "Update")
                }
            }
            Image(
                painter = if (selectedPictureUri != null) {
                    rememberImagePainter(selectedPictureUri)
                } else if (picture != ""){
                    loadImageFromBase64(base64Image = picture, context = LocalContext.current)
                } else {
                    painterResource(id = R.drawable.default_user)
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((0.5 * LocalConfiguration.current.screenHeightDp).dp)
            )
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(15.dp),
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UpdateUserPicturePreview(){
    UpdateUserPicture()
}