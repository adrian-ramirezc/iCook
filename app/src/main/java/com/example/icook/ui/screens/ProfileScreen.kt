package com.example.icook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.icook.R
import com.example.icook.data.models.Post
import com.example.icook.data.models.User
import com.example.icook.ui.components.BottomNavigationBar

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: User = User(),
    posts_counter: Int = 15,
    followers_counter: Int = 30,
    following_counter: Int = 45,
    posts: List<Post> = listOf<Post>(),
    onDescriptionTextFieldClicked: (String) -> Unit = {a: String ->},
    onHomeButtonClicked: () -> Unit = {},
    onCreatePostButtonClicked: () -> Unit = {},
    persistNewUserDescription: () -> Unit = {},
    ) {

    var isDescriptionEnabled by remember {
        mutableStateOf<Boolean>(false)
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "@${user.username}",
            modifier = Modifier.padding(15.dp)
            )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.default_user),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.DarkGray, CircleShape)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "$posts_counter")
                Text(text = "posts")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$followers_counter")
                Text(text = "followers")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$following_counter")
                Text(text = "following")
            }
        }
        Text(
            text = "${user.name} ${user.lastname}",
            modifier = Modifier.padding(start = 25.dp)
            )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(0.8f),
                maxLines = 3,
                enabled = isDescriptionEnabled,
                value = user.description,
                onValueChange ={newValue -> onDescriptionTextFieldClicked(newValue)}
            )
            IconToggleButton(
                checked = isDescriptionEnabled,
                modifier = Modifier.weight(0.2f),
                onCheckedChange = {checked ->
                    isDescriptionEnabled = !isDescriptionEnabled
                    if (!checked) {
                        persistNewUserDescription()
                    }
                }
            ) {
                Icon(
                    if (isDescriptionEnabled) {Icons.Default.Check} else {Icons.Default.Create}
                    , contentDescription = null
                )
            }
        }

        Text(
            text = "Your Posts",
            modifier = Modifier.padding(15.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 96.dp),
            modifier = Modifier.weight(1f)
        ){
            items(18) {Image(
                painter = painterResource(id = R.drawable.default_post),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )}
        }
        BottomNavigationBar(
            onHomeButtonClicked = onHomeButtonClicked,
            onCreatePostButtonClicked = onCreatePostButtonClicked,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(user=User(username = "aramirez", name = "Adrian", lastname = "Ramirez"))
}