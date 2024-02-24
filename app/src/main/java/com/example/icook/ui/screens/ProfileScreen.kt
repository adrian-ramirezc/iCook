package com.example.icook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.icook.data.models.UserPostOptions
import com.example.icook.data.models.User
import com.example.icook.ui.components.BottomNavigationBar
import com.example.icook.ui.components.FeedPostList

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: User = User(),
    posts_counter: Int = 15,
    likes_counter: Int = 30,
    posts: List<Post> = listOf(),
    onDescriptionTextFieldClicked: (String) -> Unit = {a: String ->},
    onHomeButtonClicked: () -> Unit = {},
    onCreatePostButtonClicked: () -> Unit = {},
    persistNewUserDescription: () -> Unit = {},
    onPostOptionClicked: (Post, UserPostOptions) -> Unit = { _, _ ->}
) {
    var isDescriptionEnabled by remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "@${user.username}",
            modifier = Modifier.padding(start = 25.dp, top = 15.dp)
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
                Text(text = "$likes_counter")
                Text(text = "likes")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                label = {Text(text = "${user.name} ${user.lastname}")},
                modifier = Modifier.weight(0.8f),
                maxLines = 3,
                enabled = isDescriptionEnabled,
                value = user.description,
                trailingIcon = {
                    IconToggleButton(
                        checked = true,
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
                        }},
                        onValueChange ={newValue -> onDescriptionTextFieldClicked(newValue)}
            )

        }

        Text(
            text = "Your Posts",
            modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 4.dp)
        )
        FeedPostList(
            modifier = Modifier.weight(1f),
            posts = posts,
            onPostOptionClicked = onPostOptionClicked,
            isUserPosts = true,
        )
        BottomNavigationBar(
            onHomeButtonClicked = onHomeButtonClicked,
            onCreatePostButtonClicked = onCreatePostButtonClicked,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(
        user=User(
            username = "aramirez",
            name = "Adrian",
            lastname = "Ramirez",
            description = "This is a basic description"
        ),
        posts = listOf(Post(), Post())
    )
}